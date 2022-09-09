package com.example.quotes.data.model.album

import com.example.quotes.data.model.Model
import com.google.gson.annotations.SerializedName

data class TopAlbumsResponse(
  @SerializedName("topalbums") val topAlbums: TopAlbumWrapper? = null
)
