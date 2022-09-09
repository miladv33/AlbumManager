package com.example.quotes.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quotes.data.model.album.Album
import com.example.quotes.data.model.album.TopAlbum
import com.example.quotes.data.model.artist.Artist
import kotlinx.coroutines.flow.Flow

@Dao
interface LastFMDao {

    /**
     * Get stored albums
     *
     * @return
     */
    @Query("SELECT * FROM Album ORDER BY name")
    fun getStoredAlbums(): List<Album>

    /**
     * Get album info
     *
     * @param albumName
     * @param artistName
     * @return
     */
    @Query("SELECT * FROM Album WHERE name LIKE :albumName AND artist LIKE :artistName")
    suspend fun getAlbumInfo(albumName: String?, artistName: String?): Album?

    /**
     * Insert album
     *
     * @param album
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: Album)

    /**
     * Remove album from db
     *
     * @param albumName
     * @param artistName
     * @return
     */
    @Query("DELETE FROM Album WHERE name LIKE :albumName AND artist LIKE :artistName")
    suspend fun removeAlbumFromDB(albumName: String?, artistName: String?): Int
}