package com.example.quotes.data

import androidx.room.TypeConverter
import com.example.quotes.data.model.artist.Artist
import com.example.quotes.data.model.generic.LastFMImage
import com.example.quotes.data.model.generic.TracksWrapper
import com.example.quotes.data.model.generic.Wiki
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converters {

  private val gson = Gson()

  // artist converter
  // list artist converter
  @TypeConverter
  fun artistToString(artist: Artist?): String? {
    return gson.toJson(artist)
  }

  @TypeConverter
  fun stringToArtist(artist: String?): Artist? {
    return gson.fromJson(artist, Artist::class.java)
  }

  // list artist converter
  @TypeConverter
  fun artistListToString(artistList: List<Artist>?): String? {
    return gson.toJson(artistList)
  }

  @TypeConverter
  fun stringToArtistList(artistList: String?): List<Artist>? {
    if (artistList == null) {
      return listOf()
    }
    val listType: Type = object :
      TypeToken<List<Artist>?>() {}.type
    return gson.fromJson<List<Artist>>(artistList, listType)
  }

  // LastFMImage converter
  @TypeConverter
  fun imageToString(image: LastFMImage?): String? {
    return gson.toJson(image)
  }

  @TypeConverter
  fun stringToImage(image: String?): LastFMImage? {
    return gson.fromJson(image, LastFMImage::class.java)
  }

  // List of LastFMImage
  @TypeConverter
  fun imageListToString(image: List<LastFMImage>?): String? {
    return gson.toJson(image)
  }

  @TypeConverter
  fun stringToImageList(image: String?): List<LastFMImage>? {
    if (image == null) {
      return listOf()
    }
    val listType: Type = object :
      TypeToken<List<LastFMImage>?>() {}.type
    return gson.fromJson<List<LastFMImage>>(image, listType)
  }

  // TracksWrapper converter
  @TypeConverter
  fun tracksWrapperToString(wrapper: TracksWrapper?): String? {
    return gson.toJson(wrapper)
  }

  @TypeConverter
  fun stringToTracksWrapper(wrapper: String?): TracksWrapper? {
    return gson.fromJson(wrapper, TracksWrapper::class.java)
  }

  // wiki converter
  @TypeConverter
  fun wikiToString(wiki: Wiki?): String? {
    return gson.toJson(wiki)
  }

  @TypeConverter
  fun stringToWiki(wiki: String?): Wiki? {
    return gson.fromJson(wiki, Wiki::class.java)
  }
}
