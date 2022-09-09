package com.example.quotes.presentation.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quotes.data.enum.Error
import com.example.quotes.data.model.CustomException
import com.example.quotes.data.model.artist.Artist
import com.example.quotes.data.model.generic.LastFMAttr

/**
 * Base view model
 *
 */
abstract class BaseViewModel : ViewModel() {
    /**
     * To observe dialog state on the UI, we need a liveData
     */
    private val _showErrorDialogLiveData = MutableLiveData<Boolean>()
    val showErrorDialogLiveData: LiveData<Boolean> = _showErrorDialogLiveData

    /**
     * A liveData can be observed by the UI if more details are needed about an exception.
     */
    private val _randomQuoteErrorLiveData = MutableLiveData<Throwable>()
    val randomQuoteErrorLiveData: LiveData<Throwable> = _randomQuoteErrorLiveData

    /**
     * If a service fails, UI observes a livedata and understands the error so it can display an error dialog to the user
     *
     * @param throwable
     */
    protected fun onFailure(throwable: Throwable) {
        _showErrorDialogLiveData.value = true
        if (throwable is CustomException) {
            when (throwable.error) {
                Error.NullObject -> {
                    _randomQuoteErrorLiveData.value = throwable
                }
            }
        } else {
            _randomQuoteErrorLiveData.value = throwable
        }
    }

    /**
     * Hide dialog
     *
     */
    fun hideDialog() {
        _showErrorDialogLiveData.value = false
    }

}