package com.example.quotes.data.model.generic

import com.google.gson.annotations.SerializedName

data class LastFMAttr(
  @SerializedName("artist") val artist: String? = null,
  @SerializedName("page") val page: String? = null,
  @SerializedName("perPage") val perPage: String? = null,
  @SerializedName("totalPages") val totalPages: String? = null,
  @SerializedName("total") val total: String? = null
)
