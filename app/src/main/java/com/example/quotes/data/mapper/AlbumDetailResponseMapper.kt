package com.example.quotes.data.mapper

import com.example.quotes.data.mapper.base.ResponseMapper
import com.example.quotes.data.model.album.AlbumInfo
import com.example.quotes.data.model.album.AlbumInfoWrapper
import retrofit2.Response

class AlbumDetailResponseMapper : ResponseMapper<AlbumInfoWrapper, AlbumInfo> {
    override fun createModelFromDTO(input: Response<AlbumInfoWrapper>): AlbumInfo {
        return AlbumInfo(input.body()?.album, input.body()?.attr)
    }
}