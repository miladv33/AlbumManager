package com.example.quotes.data.repository.albumRepository

import android.util.Log
import com.example.quotes.BuildConfig
import com.example.quotes.data.mapper.TopAlbumResponseMapper
import com.example.quotes.data.model.album.TopAlbumWrapper
import com.example.quotes.data.repository.base.IBaseRepository
import com.example.quotes.data.remote.service.LastFMService
import com.example.quotes.data.remote.service.TOP_ALBUMS
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Inject


@Module
@InstallIn(ViewModelComponent::class)
class TopAlbumRepository @Inject constructor(
    private val lastFMService: LastFMService,
    override val mapper: TopAlbumResponseMapper
) : IBaseRepository.TopAlbumRepository() {

    override suspend fun getTopAlbums(artistName: String?, page: Int): Result<TopAlbumWrapper> {
        val topAlbums =
            lastFMService.getTopAlbums(BuildConfig.apiKey, TOP_ALBUMS, artistName, page, 20)
        return mapper.map(topAlbums)
    }

}