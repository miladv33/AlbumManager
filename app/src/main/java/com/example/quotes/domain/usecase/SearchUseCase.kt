package com.example.quotes.domain.usecase

import com.example.quotes.data.repository.albumRepository.ArtistRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@Module
@InstallIn(ViewModelComponent::class)
class SearchUseCase @Inject constructor(
    private var artistRepository: ArtistRepository
) {
    fun executeSearchArtist(name: String, page: Int) = flow {
        emit(artistRepository.searchArtist(name, page))
    }
}