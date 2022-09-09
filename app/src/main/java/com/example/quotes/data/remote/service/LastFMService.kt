package com.example.quotes.data.remote.service

import com.example.quotes.data.model.album.AlbumInfoWrapper
import com.example.quotes.data.model.album.TopAlbumsResponse
import com.example.quotes.data.model.artist.ArtistsSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFMService {

  @GET("?format=json")
  suspend fun searchArtist(
    @Query("api_key") apiKey: String,
    @Query("method") method: String,
    @Query("artist") artist: String? = "",
    @Query("mbid") mbid: String? = "",
    @Query("page") page: Int? = 0,
    @Query("limit") limit: Int? = 0,
  ): Response<ArtistsSearchResponse>

  @GET("?format=json")
  suspend fun getTopAlbums(
    @Query("api_key") apiKey: String,
    @Query("method") method: String,
    @Query("artist") artist: String? = "",
    @Query("page") page: Int? = 0,
    @Query("limit") limit: Int? = 0,
  ): Response<TopAlbumsResponse>

  @GET("?format=json")
  suspend fun getAlbumInfo(
    @Query("api_key") apiKey: String,
    @Query("method") method: String,
    @Query("artist") artist: String? = "",
    @Query("album") album: String? = "",
  ): Response<AlbumInfoWrapper>


}