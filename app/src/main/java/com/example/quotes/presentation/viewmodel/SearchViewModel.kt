package com.example.quotes.presentation.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quotes.data.model.artist.Artist
import com.example.quotes.domain.usecase.SearchUseCase
import com.example.quotes.presentation.base.BaseViewModel
import com.example.quotes.presentation.base.SearchableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Main view model
 *
 * @property mainShowCase
 * @constructor Create empty Main view model
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : SearchableViewModel() {
    var pageTitle: MutableState<String>? = null
    var text by mutableStateOf("")
    private val _artistSearchLiveData = MutableLiveData<List<Artist>>()
    val artistSearchLiveData: LiveData<List<Artist>> = _artistSearchLiveData
    private val artistSearchList = mutableListOf<Artist>()
    fun startSearchArtist(artistName: String, startFromZero: Boolean) {
        startSearch(artistSearchList, artistName, startFromZero) { name, page ->
            searchArtist(artistName, page)
        }
    }

    private fun searchArtist(artistName: String, page: Int) {
        viewModelScope.launch {
            searchUseCase.executeSearchArtist(artistName, page).flowOn(Dispatchers.IO).collect {
                it.onSuccess { baseRandomQuote ->
                    setTotalResults(baseRandomQuote.totalResults)
                    searching.value = false
                    baseRandomQuote.artistMatches?.artistsWrapper?.let { artistWrapper ->
                        artistSearchList.addAll(artistWrapper)
                    }
                    _artistSearchLiveData.value = artistSearchList
                    restlts += artistSearchList.size
                }
                it.onFailure { throwable ->
                    searching.value = false
                    onFailure(throwable)
                }
            }
        }
    }
}