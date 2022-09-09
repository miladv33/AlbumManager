package com.example.quotes

import app.cash.turbine.test
import com.example.quotes.base.TestBase
import com.example.quotes.data.dao.LastFMDao
import com.example.quotes.data.enum.Error
import com.example.quotes.data.mapper.AlbumResponseMapper
import com.example.quotes.data.model.CustomException
import com.example.quotes.data.model.album.Album
import com.example.quotes.data.repository.albumRepository.AlbumRepository
import com.example.quotes.di.Mapper
import com.example.quotes.domain.usecase.HistoryUseCase
import com.example.quotes.presentation.viewmodel.HistoryViewModel
import com.google.common.truth.Truth.assertThat
import com.jraska.livedata.TestObserver
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import retrofit2.Response


class HistoryFlowTest : TestBase() {


   private lateinit var lastFM:LastFMDao
   private lateinit var albumResponseMapper:AlbumResponseMapper
   private lateinit var albumRepository: AlbumRepository
   private lateinit var historyUseCase: HistoryUseCase
   private lateinit var historyViewModel: HistoryViewModel

    @Before
    fun before() {
        lastFM = mock(LastFMDao::class.java)
        albumResponseMapper = spy(Mapper.provideAlbumMapper())
        albumRepository = spy(AlbumRepository(lastFM, albumResponseMapper))
        historyUseCase = spy(HistoryUseCase(albumRepository))
        historyViewModel = spy(HistoryViewModel(historyUseCase))
    }


    @Test
    fun `flow of data is working`() = runJob {
        historyViewModel.getAllSavedAlbum()
        verify(historyUseCase).executeGetAllAlbums()
        verify(albumRepository).getAllHistory()
        verify(lastFM).getStoredAlbums()
    }


    @Test
    fun `return album list from database with mapper`() = runJob {
        //arrange
        val fakeAlbumList = arrayListOf(Album("first"), Album("second"))
        Mockito.`when`(lastFM.getStoredAlbums()).thenReturn(fakeAlbumList)

        //act
        val randomFlow = albumRepository.getAllHistory()

        //assert
        assertThat(randomFlow.isSuccess).isEqualTo(true)
    }


    @Test
    fun `return api call as failure Result`() = runJob {
        //arrange
        val throwable = CustomException(Error.NullObject)
        val result = Result.failure<Album>(throwable)
        val error = Response.error<Album>(
            403,
            "{\"key\":[\"somestuff\"]}"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )

        `when`(albumResponseMapper.map(error)).thenReturn(result)

        //act
        historyViewModel.getAllSavedAlbum()
        //assert

        historyUseCase.executeGetAllAlbums().test {
            val item = awaitItem()
            assertThat(item.isFailure).isTrue()
            assertThat(item.exceptionOrNull()).isEqualTo(throwable)
            cancelAndConsumeRemainingEvents()
        }
    }


    @Test
    fun `change dialog visibility`() {
        historyViewModel.hideDialog()
        TestObserver.test(historyViewModel.showErrorDialogLiveData)
            .assertValue(false)
    }
}