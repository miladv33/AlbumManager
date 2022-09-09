package com.example.quotes.data.model.artist

import com.example.quotes.data.model.artist.ArtistSearch
import com.google.gson.annotations.SerializedName

data class ArtistsSearchResponse(
  @SerializedName("results") val results: ArtistSearch? = null
)
