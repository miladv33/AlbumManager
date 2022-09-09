package com.example.quotes

import app.cash.turbine.test
import com.example.quotes.base.TestBase
import com.example.quotes.data.dao.LastFMDao
import com.example.quotes.data.enum.Error
import com.example.quotes.data.mapper.AlbumResponseMapper
import com.example.quotes.data.mapper.LastFMResponseMapper
import com.example.quotes.data.mapper.TopAlbumResponseMapper
import com.example.quotes.data.model.CustomException
import com.example.quotes.data.model.album.Album
import com.example.quotes.data.model.album.TopAlbum
import com.example.quotes.data.model.album.TopAlbumWrapper
import com.example.quotes.data.model.album.TopAlbumsResponse
import com.example.quotes.data.model.artist.Artist
import com.example.quotes.data.model.artist.ArtistSearch
import com.example.quotes.data.model.artist.ArtistsSearchResponse
import com.example.quotes.data.model.artist.ArtistsWrapper
import com.example.quotes.data.remote.service.ARTIST_SEARCH
import com.example.quotes.data.remote.service.LastFMService
import com.example.quotes.data.remote.service.TOP_ALBUMS
import com.example.quotes.data.repository.albumRepository.AlbumRepository
import com.example.quotes.data.repository.albumRepository.ArtistRepository
import com.example.quotes.data.repository.albumRepository.TopAlbumRepository
import com.example.quotes.di.AppModule
import com.example.quotes.di.Mapper
import com.example.quotes.domain.usecase.HistoryUseCase
import com.example.quotes.domain.usecase.SearchUseCase
import com.example.quotes.domain.usecase.TopAlbumUseCase
import com.example.quotes.presentation.viewmodel.HistoryViewModel
import com.example.quotes.presentation.viewmodel.SearchViewModel
import com.example.quotes.presentation.viewmodel.TopAlbumViewModel
import com.google.common.truth.Truth.assertThat
import com.jraska.livedata.TestObserver
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import retrofit2.Response
import retrofit2.Retrofit


class TopAlbumFlowTest : TestBase() {


   private lateinit var lastFMService: LastFMService
   private lateinit var topAlbumResponseMapper: TopAlbumResponseMapper
   private lateinit var topAlbumRepository: TopAlbumRepository
   private lateinit var topAlbumUseCase: TopAlbumUseCase
   private lateinit var topAlbumViewModel: TopAlbumViewModel

    @Before
    fun before() {
        val retrofit = AppModule.provideQuotableServerRetrofit()
        lastFMService = spy(retrofit.create(LastFMService::class.java))
        topAlbumResponseMapper = spy(TopAlbumResponseMapper())
        topAlbumRepository = spy(TopAlbumRepository(lastFMService, topAlbumResponseMapper))
        topAlbumUseCase= spy(TopAlbumUseCase(topAlbumRepository))
        topAlbumViewModel = spy(TopAlbumViewModel(topAlbumUseCase))
    }


    @Test
    fun `flow of data is working`() = runJob {
        topAlbumViewModel.startGetTopAlbums("",true)
        verify(topAlbumUseCase).executeGetTopAlbums("", 1)
        verify(topAlbumRepository).getTopAlbums("",1)
        verify(lastFMService).getTopAlbums(BuildConfig.apiKey, TOP_ALBUMS, "", page = 1, limit = 20)
    }


    @Test
    fun `return artist list from server`() = runJob {
        //arrange
        val artists = arrayListOf(TopAlbum(url = "", artist = Artist(url = "", name = "Nirvana")))
        val topAlbumWrapper = TopAlbumWrapper(artists)
        val mockResponse: Response<TopAlbumsResponse>? =
            Response.success(TopAlbumsResponse(topAlbumWrapper),okhttp3.Response.Builder()
                    .code(200)
                    .message("Response.success()")
                    .protocol(Protocol.HTTP_1_1)
                    .request(Request.Builder().url("http://test-url/").build())
                    .receivedResponseAtMillis(1619053449513)
                    .sentRequestAtMillis(1619053443814)
                    .build())
        `when`(lastFMService.getTopAlbums(BuildConfig.apiKey, TOP_ALBUMS, "", page = 1, limit = 20))
            .thenReturn(mockResponse)

        //act
        val randomFlow = topAlbumRepository.getTopAlbums("",1)

        //assert
        assertThat(randomFlow.isSuccess).isEqualTo(true)
        assertThat(randomFlow.getOrNull()?.albumList?.get(0)?.artist?.name).isEqualTo(artists.first().artist?.name)
    }


    @Test
    fun `return api call as failure Result`() = runBlocking {
        //arrange
        val throwable = CustomException(Error.NullObject)
        val result = Result.failure<TopAlbumWrapper>(throwable)
        val error = Response.error<TopAlbumsResponse>(
            403,
            "{\"key\":[\"somestuff\"]}"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        `when`(lastFMService.getTopAlbums(BuildConfig.apiKey, TOP_ALBUMS, "", page = 1, limit = 20))
            .thenReturn(error)

        //act
        topAlbumViewModel.startGetTopAlbums("",true)
        //assert
        topAlbumUseCase.executeGetTopAlbums("",1).test {
            val item = awaitItem()
            assertThat(item.isFailure).isTrue()
            assertThat(item.exceptionOrNull()).isEqualTo(throwable)
            cancelAndConsumeRemainingEvents()
        }
    }
}