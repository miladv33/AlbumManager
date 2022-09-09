package com.example.quotes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quotes.data.model.album.Album
import com.example.quotes.data.model.album.AlbumInfo
import com.example.quotes.domain.usecase.DetailUseCase
import com.example.quotes.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailUseCase: DetailUseCase
) : BaseViewModel() {
    private val _detailLiveData = MutableLiveData<AlbumInfo>()
    val detailLiveData: LiveData<AlbumInfo> = _detailLiveData

    /**
     * Get quotes flow
     *
     */
    fun getAlbumDetail(artistName: String, albumName: String) {
        viewModelScope.launch {
            detailUseCase.executeGetAlbum(artistName, albumName).flowOn(Dispatchers.IO).take(2)
                .collect {
                    it.onSuccess { baseRandomQuote ->
                        _detailLiveData.value = baseRandomQuote
                    }
                    it.onFailure { throwable ->
                        onFailure(throwable)
                    }
                }
        }
    }

    fun saveAlbum(album: Album?) {
        if (album == null) return
        viewModelScope.launch {
            detailUseCase.saveAlbum(album)
        }
    }

    fun removeAlbum(albumName: String?, artistName: String?) {
        viewModelScope.launch {
            detailUseCase.removeAlbumFromDatabase(albumName, artistName)
        }
    }

}