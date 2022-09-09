package com.example.quotes.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quotes.data.enum.Error
import com.example.quotes.data.model.CustomException
import com.example.quotes.data.model.album.Album
import com.example.quotes.domain.usecase.HistoryUseCase
import com.example.quotes.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject
constructor(var historyUseCase: HistoryUseCase) : BaseViewModel() {
    private val _albumLiveData = MutableLiveData<List<Album>>()
    val albumLiveData: LiveData<List<Album>> = _albumLiveData

    fun getAllSavedAlbum() {
        viewModelScope.launch {
            historyUseCase.executeGetAllAlbums().flowOn(Dispatchers.IO).collect {
                it.onSuccess { baseRandomQuote ->
                    _albumLiveData.value = baseRandomQuote
                }
                it.onFailure { throwable ->
                    if (throwable is CustomException) {
                        if (throwable.error == Error.NullObject)
                            _albumLiveData.value = arrayListOf()
                    }
                    onFailure(throwable)
                }
            }
        }
    }
}