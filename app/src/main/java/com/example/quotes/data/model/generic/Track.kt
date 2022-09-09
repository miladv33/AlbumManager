package com.example.quotes.data.model.generic

import com.google.gson.annotations.SerializedName
import com.example.quotes.data.model.artist.Artist
import com.example.quotes.data.model.generic.Streamable

data class Track(
  @SerializedName("streamable") val streamable: Streamable? = null,
  @SerializedName("duration") val duration: Int? = null,
  @SerializedName("url") val url: String? = null,
  @SerializedName("name") val name: String? = null,
  @SerializedName("artist") val artist: Artist? = null
)
