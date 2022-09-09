package com.example.quotes.di

import com.example.quotes.data.mapper.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object Mapper {

    @Provides
    fun provideLastFMMapper(): LastFMResponseMapper {
        return LastFMResponseMapper()
    }

    @Provides
    fun provideTOPAlbumMapper(): TopAlbumResponseMapper {
        return TopAlbumResponseMapper()
    }

    @Provides
    fun provideAlbumDetailMapper(): AlbumDetailResponseMapper {
        return AlbumDetailResponseMapper()
    }

    @Provides
    fun provideAlbumMapper(): AlbumResponseMapper {
        return AlbumResponseMapper()
    }

    @Provides
    fun provideAlbumDetailModelMapper(): AlbumDetailModelMapper {
        return AlbumDetailModelMapper()
    }
}