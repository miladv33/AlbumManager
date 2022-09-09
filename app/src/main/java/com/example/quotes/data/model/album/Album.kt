package com.example.quotes.data.model.album

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.quotes.data.model.Model
import com.google.gson.annotations.SerializedName
import com.example.quotes.data.model.generic.LastFMImage
import com.example.quotes.data.model.generic.TracksWrapper
import com.example.quotes.data.model.generic.Wiki


@Entity
data class Album(
  @PrimaryKey
  @NonNull
  @SerializedName("url")
  var url: String,
  @SerializedName("artist")
  var artist: String? = null,
  @SerializedName("mbid")
  var mbid: String? = null,
  @SerializedName("playcount")
  var playcount: Int? = null,
  @SerializedName("image")
  var image: List<LastFMImage> = listOf(),
  @SerializedName("tracks")
  var tracks: TracksWrapper? = null,
  @SerializedName("name")
  var name: String? = null,
  @SerializedName("listeners")
  var listeners: String? = null,
  @SerializedName("wiki")
  var wiki: Wiki? = null
):Model() {
  var isFavorite = false
  var imagePath: String? = null

}
