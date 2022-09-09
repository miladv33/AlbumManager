package com.example.quotes.data.model.generic

import com.google.gson.annotations.SerializedName

data class Streamable(
  @SerializedName("fulltrack") val fulltrack: String? = null,
  @SerializedName("#text") val text: String? = null
)
