package com.example.quotes.data.model.generic

import com.example.quotes.data.model.Model
import com.google.gson.annotations.SerializedName

data class TracksWrapper(
  @SerializedName("track") val track: List<Track> = listOf()
): Model()
