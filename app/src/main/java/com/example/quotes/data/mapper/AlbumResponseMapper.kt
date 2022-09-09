package com.example.quotes.data.mapper

import com.example.quotes.data.enum.Error
import com.example.quotes.data.mapper.base.ListResponseMapper
import com.example.quotes.data.model.CustomException
import com.example.quotes.data.model.album.Album
import retrofit2.Response

class AlbumResponseMapper : ListResponseMapper<Album, Album> {
    override fun createModelFromDTO(input: Response<Album>): Album {
        val album = input.body()?.let {
            Album(
                it.url,
                it.artist,
                it.mbid,
                it.playcount,
                it.image,
                it.tracks,
                it.name,
                it.listeners,
                it.wiki
            )
        }
        return album ?: Album("")
    }

    override fun mapList(input: List<Album>?): Result<List<Album>> {
        return if (input == null || input.isEmpty()) {
            Result.failure(CustomException(Error.NullObject))
        } else {
            val result = ArrayList<Album>()
            input.forEach {
                result.add(
                    Album(
                        it.url,
                        it.artist,
                        it.mbid,
                        it.playcount,
                        it.image,
                        it.tracks,
                        it.name,
                        it.listeners,
                        it.wiki
                    )
                )
            }
            Result.success(result)
        }
    }




}