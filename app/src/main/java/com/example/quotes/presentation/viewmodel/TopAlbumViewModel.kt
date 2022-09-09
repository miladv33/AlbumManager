package com.example.quotes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quotes.data.model.album.TopAlbum
import com.example.quotes.domain.usecase.TopAlbumUseCase
import com.example.quotes.presentation.base.BaseViewModel
import com.example.quotes.presentation.base.SearchableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Main view model
 *
 * @property mainShowCase
 * @constructor Create empty Main view model
 */
@HiltViewModel
class TopAlbumViewModel @Inject constructor(
    private val mainShowCase: TopAlbumUseCase
) : SearchableViewModel() {

    var artistName: String = ""
    private val _topAlbumsLiveData = MutableLiveData<List<TopAlbum>>()
    val topAlbumsLiveData: LiveData<List<TopAlbum>> = _topAlbumsLiveData
    private val topAlbumList = mutableListOf<TopAlbum>()


    fun startGetTopAlbums(artistName: String, startFromZero: Boolean) {
        startSearch(topAlbumList, artistName, startFromZero){ _, page->
            getTopAlbums(artistName, page)
        }
    }

    private fun getTopAlbums(artistName: String, page: Int) {
        Log.i("miladCheckCall", "getAlbumDetail: called")
        viewModelScope.launch {
            mainShowCase.executeGetTopAlbums(artistName, page).flowOn(Dispatchers.IO).collect {
                it.onSuccess { baseRandomQuote ->
                    setTotalResults(baseRandomQuote.attr)
                    topAlbumList.addAll(baseRandomQuote.albumList ?: arrayListOf())
                    _topAlbumsLiveData.value = topAlbumList
                    searching.value = false

                }
                it.onFailure { throwable ->
                    searching.value = false
                    onFailure(throwable)
                }
            }
        }
    }
}