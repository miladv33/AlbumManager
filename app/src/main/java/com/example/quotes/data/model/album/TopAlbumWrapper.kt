package com.example.quotes.data.model.album

import com.example.quotes.data.model.Model
import com.google.gson.annotations.SerializedName
import com.example.quotes.data.model.generic.LastFMAttr

data class TopAlbumWrapper(
    @SerializedName("album") val albumList: List<TopAlbum>? = null,
    @SerializedName("@attr") val attr: LastFMAttr? = null
) : Model()