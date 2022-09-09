package com.example.quotes.data.model.error

import com.google.gson.annotations.SerializedName

class ErrorResponse(
  @SerializedName("message") val message: String? = null,
  @SerializedName("error") val error: Int? = null
)