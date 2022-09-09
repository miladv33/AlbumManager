package com.example.quotes.data.repository.base

import com.example.quotes.data.model.album.Album
import com.example.quotes.data.model.album.AlbumInfo
import com.example.quotes.data.model.album.AlbumInfoWrapper
import com.example.quotes.data.model.album.TopAlbumWrapper
import com.example.quotes.data.model.artist.ArtistSearch

/**
 * Base repository. Including all base repositories. In this way, we can use the same name for both parent and child.
 * The ArtistRepository doesn't have to be named ArtistRepositoryImpl, for example.
 * ArtistRepository can be inherited from IBaseRepository.ArtistRepository
 * It's so much cleaner this way.
 *
 */
interface IBaseRepository {
    /**
     * Artist repository
     *
     */
    abstract class ArtistRepository : SafeCallRepository<ArtistSearch>() {
        /**
         * Search artist by name
         *
         * @param name
         * @param page
         * @return
         */
        abstract suspend fun searchArtist(name: String, page: Int): Result<ArtistSearch>
    }

    /**
     * Top album repository
     *
     * @constructor Create empty Top album repository
     */
    abstract class TopAlbumRepository : SafeCallRepository<TopAlbumWrapper>() {
        /**
         * Get top albums by artist name
         *
         * @param artistName
         * @param page
         * @return
         */
        abstract suspend fun getTopAlbums(artistName: String?, page: Int): Result<TopAlbumWrapper>
    }

    /**
     * Album detail repository
     *
     * @constructor Create empty Album detail repository
     */
    abstract class AlbumDetailRepository : SafeCallRepository<AlbumInfo>() {
        /**
         * Get album details
         *
         * @param artistName
         * @param albumName
         * @return
         */
        abstract suspend fun getAlbumDetails(artistName: String?, albumName: String): Result<AlbumInfo>

        /**
         * Get album from database
         *
         * @param albumName
         * @param artistName
         * @return
         */
        abstract suspend fun getAlbumFromDatabase(albumName: String?, artistName: String?):Result<AlbumInfo>
    }

    /**
     * Album repository
     *
     * @constructor Create empty Album repository
     */
    abstract class AlbumRepository : SafeCallRepository<Album>() {
        /**
         * Insert album in database
         *
         * @param album
         */
        abstract suspend fun insertAlbumInDatabase(album: Album)

        /**
         * Get all history
         *
         * @return
         */
        abstract suspend fun getAllHistory(): Result<List<Album>>

        /**
         * Remove album from database
         *
         * @param albumName
         * @param artistName
         */
        abstract suspend fun removeAlbumFromDatabase(albumName: String?, artistName: String?)
    }

}