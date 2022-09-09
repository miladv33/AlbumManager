package com.example.quotes.data.repository.albumRepository

import com.example.quotes.BuildConfig
import com.example.quotes.data.dao.LastFMDao
import com.example.quotes.data.mapper.AlbumDetailModelMapper
import com.example.quotes.data.mapper.AlbumDetailResponseMapper
import com.example.quotes.data.model.album.AlbumInfo
import com.example.quotes.data.remote.service.ALBUM_INFO
import com.example.quotes.data.remote.service.LastFMService
import com.example.quotes.data.repository.base.IBaseRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Inject

@Module
@InstallIn(ViewModelComponent::class)
class AlbumDetailRepository @Inject constructor(
    private val lastFMService: LastFMService,
    private val lastFMDao: LastFMDao,
    override val mapper: AlbumDetailResponseMapper,
    private val albumDetailModelMapper: AlbumDetailModelMapper
) : IBaseRepository.AlbumDetailRepository() {

    override suspend fun getAlbumDetails(
        artistName: String?,
        albumName: String
    ): Result<AlbumInfo> = safeCall {
        val albumInfo =
            lastFMService.getAlbumInfo(BuildConfig.apiKey, ALBUM_INFO, artistName, albumName)
        mapper.map(albumInfo)
    }

    override suspend fun getAlbumFromDatabase(
        albumName: String?,
        artistName: String?
    ): Result<AlbumInfo> = safeCall {
        val albumInfo = lastFMDao.getAlbumInfo(albumName, artistName)
        albumDetailModelMapper.map(albumInfo)
    }

}