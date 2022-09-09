package com.example.quotes

import app.cash.turbine.test
import com.example.quotes.base.TestBase
import com.example.quotes.data.dao.LastFMDao
import com.example.quotes.data.enum.Error
import com.example.quotes.data.mapper.AlbumResponseMapper
import com.example.quotes.data.mapper.LastFMResponseMapper
import com.example.quotes.data.model.CustomException
import com.example.quotes.data.model.album.Album
import com.example.quotes.data.model.artist.Artist
import com.example.quotes.data.model.artist.ArtistSearch
import com.example.quotes.data.model.artist.ArtistsSearchResponse
import com.example.quotes.data.model.artist.ArtistsWrapper
import com.example.quotes.data.remote.service.ARTIST_SEARCH
import com.example.quotes.data.remote.service.LastFMService
import com.example.quotes.data.repository.albumRepository.AlbumRepository
import com.example.quotes.data.repository.albumRepository.ArtistRepository
import com.example.quotes.di.AppModule
import com.example.quotes.di.Mapper
import com.example.quotes.domain.usecase.HistoryUseCase
import com.example.quotes.domain.usecase.SearchUseCase
import com.example.quotes.presentation.viewmodel.HistoryViewModel
import com.example.quotes.presentation.viewmodel.SearchViewModel
import com.google.common.truth.Truth.assertThat
import com.jraska.livedata.TestObserver
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


class SearchFlowTest : TestBase() {


   private lateinit var lastFMService: LastFMService
   private lateinit var lastFMResponseMapper: LastFMResponseMapper
   private lateinit var artistRepository: ArtistRepository
   private lateinit var searchUseCase: SearchUseCase
   private lateinit var searchViewModel: SearchViewModel

    @Before
    fun before() {
        val retrofit = AppModule.provideQuotableServerRetrofit()
        lastFMService = spy(retrofit.create(LastFMService::class.java))
        lastFMResponseMapper = spy(LastFMResponseMapper())
        artistRepository = spy(ArtistRepository(lastFMService, lastFMResponseMapper))
        searchUseCase = spy(SearchUseCase(artistRepository))
        searchViewModel = spy(SearchViewModel(searchUseCase))
    }


    @Test
    fun `flow of data is working`() = runJob {
        searchViewModel.startSearchArtist("",true)
        verify(searchUseCase).executeSearchArtist("", 1)
        verify(artistRepository).searchArtist("",1)
        verify(lastFMService).searchArtist(BuildConfig.apiKey, ARTIST_SEARCH, "", page = 1, limit = 20)
    }


    @Test
    fun `return artist list from server`() = runJob {
        //arrange
        val artists = arrayListOf(Artist(url = "First", name = "Nirvana"))
        val artistSearch = ArtistSearch(totalResults = "1", startIndex = "0", itemsPerPage = "1", artistMatches = ArtistsWrapper(artists))
        val mockResponse: Response<ArtistsSearchResponse>? =
            Response.success(ArtistsSearchResponse(artistSearch),okhttp3.Response.Builder()
                    .code(200)
                    .message("Response.success()")
                    .protocol(Protocol.HTTP_1_1)
                    .request(Request.Builder().url("http://test-url/").build())
                    .receivedResponseAtMillis(1619053449513)
                    .sentRequestAtMillis(1619053443814)
                    .build())
        `when`(lastFMService.searchArtist(BuildConfig.apiKey, ARTIST_SEARCH, "", page = 1, limit = 20))
            .thenReturn(mockResponse)

        //act
        val randomFlow = artistRepository.searchArtist("",1)

        //assert
        assertThat(randomFlow.isSuccess).isEqualTo(true)
        assertThat(randomFlow.getOrNull()?.artistMatches?.artistsWrapper).isEqualTo(artists)
    }


    @Test
    fun `return api call as failure Result`() = runJob {
        //arrange
        val throwable = CustomException(Error.NullObject)
        val result = Result.failure<ArtistSearch>(throwable)
        val error = Response.error<ArtistsSearchResponse>(
            403,
            "{\"key\":[\"somestuff\"]}"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )

        `when`(lastFMResponseMapper.map(error)).thenReturn(result)

        //act
        searchViewModel.startSearchArtist("",false)
        //assert
        searchUseCase.executeSearchArtist("",1).test {
            val item = awaitItem()
            assertThat(item.isFailure).isTrue()
            assertThat(item.exceptionOrNull()).isEqualTo(throwable)
            cancelAndConsumeRemainingEvents()
        }
    }
}