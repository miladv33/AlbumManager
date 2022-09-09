package com.example.quotes.data.model.artist

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.example.quotes.data.model.generic.LastFMImage

@Entity
class Artist(
  @PrimaryKey(autoGenerate = true)
  var id: Long? = null,
  @SerializedName("url") @NonNull val url: String,
  @SerializedName("mbid") val mbid: String? = null,
  @ColumnInfo(collate = ColumnInfo.NOCASE) @SerializedName("name") val name: String? = null,
  @SerializedName("listeners") val listeners: String? = null,
  @SerializedName("streamable") val streamable: String? = null,
  @SerializedName("image") val image: List<LastFMImage>? = listOf()
) {

  override fun equals(other: Any?): Boolean {
    return if (other is Artist)
      url == other.url && mbid == other.mbid && name == other.name
    else
      super.equals(other)
  }

  override fun hashCode(): Int {
    var result = url.hashCode()
    result = 31 * result + (mbid?.hashCode() ?: 0)
    result = 31 * result + (name?.hashCode() ?: 0)
    return result
  }

}
