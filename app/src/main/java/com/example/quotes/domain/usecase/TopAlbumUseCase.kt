package com.example.quotes.domain.usecase

import com.example.quotes.data.repository.albumRepository.TopAlbumRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


@Module
@InstallIn(ViewModelComponent::class)
class TopAlbumUseCase @Inject constructor(
    private val topAlbumRepository: TopAlbumRepository
) {
    fun executeGetTopAlbums(artistName: String, page: Int) = flow {
        emit(topAlbumRepository.getTopAlbums(artistName, page))
    }
}
