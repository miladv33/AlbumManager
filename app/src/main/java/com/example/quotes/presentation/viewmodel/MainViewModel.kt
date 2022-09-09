package com.example.quotes.presentation.viewmodel

import com.example.quotes.domain.usecase.TopAlbumUseCase
import com.example.quotes.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Main view model
 *
 * @property mainShowCase
 * @constructor Create empty Main view model
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainShowCase: TopAlbumUseCase
) : BaseViewModel()