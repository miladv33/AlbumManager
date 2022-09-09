package com.example.quotes.data.mapper

import com.example.quotes.data.mapper.base.ResponseMapper
import com.example.quotes.data.model.album.TopAlbumWrapper
import com.example.quotes.data.model.album.TopAlbumsResponse
import retrofit2.Response

class TopAlbumResponseMapper : ResponseMapper<TopAlbumsResponse, TopAlbumWrapper> {
    override fun createModelFromDTO(input: Response<TopAlbumsResponse>): TopAlbumWrapper {
        return input.body()?.topAlbums ?: TopAlbumWrapper()
    }
}