package com.example.quotes.data.model.artist

import com.example.quotes.data.model.Model
import com.google.gson.annotations.SerializedName

data class ArtistsWrapper(
  @SerializedName("artist") val artistsWrapper: List<Artist>? = null
): Model()
