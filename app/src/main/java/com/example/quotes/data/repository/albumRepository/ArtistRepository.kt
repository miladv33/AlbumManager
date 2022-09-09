package com.example.quotes.data.repository.albumRepository

import com.example.quotes.BuildConfig
import com.example.quotes.data.mapper.LastFMResponseMapper
import com.example.quotes.data.model.artist.ArtistSearch
import com.example.quotes.data.repository.base.IBaseRepository
import com.example.quotes.data.remote.service.ARTIST_SEARCH
import com.example.quotes.data.remote.service.LastFMService
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Inject

@Module
@InstallIn(ViewModelComponent::class)
class ArtistRepository @Inject constructor(
    private val lastFMService: LastFMService,
    override val mapper: LastFMResponseMapper
) : IBaseRepository.ArtistRepository() {


    override suspend fun searchArtist(name: String, page: Int): Result<ArtistSearch> = safeCall {
        val searchArtist = lastFMService.searchArtist(
            BuildConfig.apiKey,
            ARTIST_SEARCH,
            artist = name,
            page = page,
            limit = 20
        )
        mapper.map(searchArtist)
    }
}