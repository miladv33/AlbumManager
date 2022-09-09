package com.example.quotes.data.model.album

import com.example.quotes.data.model.Model
import com.example.quotes.data.model.generic.LastFMAttr
import com.google.gson.annotations.SerializedName

data class AlbumInfo(
    @SerializedName("album") val album: Album? = null,
    @SerializedName("@attr") val attr: LastFMAttr? = null
) : Model()
