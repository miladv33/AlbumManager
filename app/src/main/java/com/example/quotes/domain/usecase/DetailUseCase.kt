package com.example.quotes.domain.usecase

import com.example.quotes.data.model.album.Album
import com.example.quotes.data.repository.albumRepository.AlbumDetailRepository
import com.example.quotes.data.repository.albumRepository.AlbumRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * provide get, save and remove an album.
 *
 * @property albumRepository
 * @property albumDetailRepository
 * @constructor Create empty Detail use case
 */
@Module
@InstallIn(ViewModelComponent::class)
class DetailUseCase @Inject constructor(
    private val albumRepository: AlbumRepository,
    private val albumDetailRepository: AlbumDetailRepository
) {
    /**
     * Check the database for data and return it if there is any.
     * If the database does not contain any data, try getting the data from the server.
     *
     * @param artistName
     * @param albumName
     */
    fun executeGetAlbum(artistName: String, albumName: String) = flow {
        val albumFromDatabase = albumDetailRepository.getAlbumFromDatabase(albumName, artistName)
        emit(albumFromDatabase)
        if (albumFromDatabase.isFailure) {
            emit(albumDetailRepository.getAlbumDetails(artistName, albumName))
        }
    }

    /**
     * Save album to database
     *
     * @param album
     */
    suspend fun saveAlbum(album: Album) {
        albumRepository.insertAlbumInDatabase(album)
    }

    /**
     * Remove album from database
     *
     * @param albumName
     * @param artistName
     */
    suspend fun removeAlbumFromDatabase(albumName: String?, artistName: String?) {
        albumRepository.removeAlbumFromDatabase(albumName, artistName)
    }
}