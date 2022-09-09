package com.example.quotes.data.model.generic

import com.google.gson.annotations.SerializedName

data class Wiki(
    @SerializedName("published") val published: String? = null,
    @SerializedName("summary") val summary: String? = null,
    @SerializedName("content") val content: String? = null
)