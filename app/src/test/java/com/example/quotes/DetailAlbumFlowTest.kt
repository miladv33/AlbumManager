package com.example.quotes

import app.cash.turbine.test
import com.example.quotes.base.TestBase
import com.example.quotes.data.dao.LastFMDao
import com.example.quotes.data.enum.Error
import com.example.quotes.data.mapper.AlbumDetailModelMapper
import com.example.quotes.data.mapper.AlbumDetailResponseMapper
import com.example.quotes.data.mapper.AlbumResponseMapper
import com.example.quotes.data.model.CustomException
import com.example.quotes.data.model.album.Album
import com.example.quotes.data.model.album.AlbumInfoWrapper
import com.example.quotes.data.remote.service.ALBUM_INFO
import com.example.quotes.data.remote.service.LastFMService
import com.example.quotes.data.repository.albumRepository.AlbumDetailRepository
import com.example.quotes.data.repository.albumRepository.AlbumRepository
import com.example.quotes.di.AppModule
import com.example.quotes.domain.usecase.DetailUseCase
import com.example.quotes.presentation.viewmodel.DetailViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Response


class DetailAlbumFlowTest : TestBase() {


    private lateinit var lastFMService: LastFMService
    private lateinit var lastFM: LastFMDao
    private lateinit var albumDetailResponseMapper: AlbumDetailResponseMapper
    private lateinit var albumDetailModelMapper: AlbumDetailModelMapper
    private lateinit var albumResponseMapper: AlbumResponseMapper
    private lateinit var albumRepository: AlbumRepository
    private lateinit var albumDetailRepository: AlbumDetailRepository
    private lateinit var detailUseCase: DetailUseCase
    private lateinit var detailViewModel: DetailViewModel

    @Before
    fun before() {
        val retrofit = AppModule.provideQuotableServerRetrofit()
        lastFMService = spy(retrofit.create(LastFMService::class.java))
        lastFM = mock(LastFMDao::class.java)
        albumDetailResponseMapper = spy(AlbumDetailResponseMapper())
        albumDetailModelMapper = spy(AlbumDetailModelMapper())
        albumResponseMapper = spy(AlbumResponseMapper())
        albumRepository = spy(AlbumRepository(lastFM, albumResponseMapper))
        albumDetailRepository = spy(
            AlbumDetailRepository(
                lastFMService,
                lastFM,
                albumDetailResponseMapper,
                albumDetailModelMapper
            )
        )
        detailUseCase = spy(DetailUseCase(albumRepository, albumDetailRepository))
        detailViewModel = spy(DetailViewModel(detailUseCase))
    }


    @Test
    fun `getAlbumDetail is working`() = runJob {
        detailViewModel.getAlbumDetail(anyString(), anyString())
        verify(detailUseCase).executeGetAlbum(anyString(), anyString())
        verify(albumDetailRepository).getAlbumFromDatabase(anyString(), anyString())
        verify(lastFM).getAlbumInfo(anyString(), anyString())
    }

    @Test
    fun `saveAlbum is working`() = runJob {
        val album = Album(url = "", name = "")
        detailViewModel.saveAlbum(album)
        verify(detailUseCase).saveAlbum(album)
        verify(albumRepository).insertAlbumInDatabase(album)
        verify(lastFM).insertAlbum(album)
    }

    @Test
    fun `removeAlbum is working`() = runJob {
        detailViewModel.removeAlbum(anyString(), anyString())
        verify(detailUseCase).removeAlbumFromDatabase(any(), any())
        verify(albumRepository).removeAlbumFromDatabase(any(), any())
        verify(lastFM).removeAlbumFromDB(any(), any())
    }

    @Test
    fun `return artist list from server`() = runJob {
        //arrange
        val album = Album(url = "")
        val mockResponse: Response<AlbumInfoWrapper>? =
            Response.success(
                AlbumInfoWrapper(album), okhttp3.Response.Builder()
                    .code(200)
                    .message("Response.success()")
                    .protocol(Protocol.HTTP_1_1)
                    .request(Request.Builder().url("http://test-url/").build())
                    .receivedResponseAtMillis(1619053449513)
                    .sentRequestAtMillis(1619053443814)
                    .build()
            )
        `when`(lastFMService.getAlbumInfo(BuildConfig.apiKey, ALBUM_INFO, ""))
            .thenReturn(mockResponse)

        //act
        val randomFlow = albumDetailRepository.getAlbumDetails("", "")

        //assert
        assertThat(randomFlow.isSuccess).isEqualTo(true)
        assertThat(randomFlow.getOrNull()?.album?.url).isEqualTo(album.url)
    }


    @Test
    fun `return api call as failure Result`() = runJob {
        //arrange
        val throwable = CustomException(Error.NullObject)
        val error = Response.error<AlbumInfoWrapper>(
            403,
            "{\"key\":[\"somestuff\"]}"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        `when`(lastFMService.getAlbumInfo(BuildConfig.apiKey, ALBUM_INFO, ""))
            .thenReturn(error)

        //act
        detailViewModel.getAlbumDetail("", "")
        //assert
        detailUseCase.executeGetAlbum("", "").test {
            val item = awaitItem()
            assertThat(item.isFailure).isTrue()
            assertThat(item.exceptionOrNull()).isEqualTo(throwable)
            cancelAndConsumeRemainingEvents()
        }
    }
}