package com.example.quotes.domain.usecase

import com.example.quotes.data.repository.albumRepository.AlbumRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@Module
@InstallIn(ViewModelComponent::class)
class HistoryUseCase @Inject constructor(private val albumRepository: AlbumRepository) {
    fun executeGetAllAlbums() = flow {
        emit(albumRepository.getAllHistory())
    }
}