package com.example.quotes.data.repository.albumRepository

import com.example.quotes.data.dao.LastFMDao
import com.example.quotes.data.mapper.AlbumResponseMapper
import com.example.quotes.data.model.album.Album
import com.example.quotes.data.repository.base.IBaseRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Inject

@Module
@InstallIn(ViewModelComponent::class)
class AlbumRepository @Inject constructor(
    private val lastFMDao: LastFMDao,
    override val mapper: AlbumResponseMapper
) : IBaseRepository.AlbumRepository() {
    override suspend fun insertAlbumInDatabase(album: Album) {
        lastFMDao.insertAlbum(album)
    }

    override suspend fun getAllHistory(): Result<List<Album>> = safeListCall {
        val storedAlbums = lastFMDao.getStoredAlbums()
        mapper.mapList(storedAlbums)
    }

    override suspend fun removeAlbumFromDatabase(albumName: String?, artistName: String?) {
        lastFMDao.removeAlbumFromDB(albumName, artistName)
    }

}