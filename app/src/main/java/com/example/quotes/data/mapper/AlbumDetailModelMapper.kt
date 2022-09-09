package com.example.quotes.data.mapper

import com.example.quotes.data.mapper.base.ModelMapper
import com.example.quotes.data.mapper.base.ResponseMapper
import com.example.quotes.data.model.album.Album
import com.example.quotes.data.model.album.AlbumInfo
import com.example.quotes.data.model.album.AlbumInfoWrapper
import retrofit2.Response

class AlbumDetailModelMapper : ModelMapper<Album, AlbumInfo> {
    override fun createModelFromDTO(input: Album): AlbumInfo {
        input.isFavorite = true
        return AlbumInfo(input)
    }
}