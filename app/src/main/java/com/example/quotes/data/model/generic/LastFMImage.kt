package com.example.quotes.data.model.generic

import com.google.gson.annotations.SerializedName

data class LastFMImage(
  @SerializedName("#text") val text: String? = null,
  @SerializedName("size") val size: String? = null
)
