package com.example.quotes.di

import com.example.quotes.BuildConfig
import com.example.quotes.data.dao.LastFMDao
import com.example.quotes.data.mapper.*
import com.example.quotes.data.remote.service.LastFMService
import com.example.quotes.data.repository.albumRepository.AlbumDetailRepository
import com.example.quotes.data.repository.albumRepository.AlbumRepository
import com.example.quotes.data.repository.albumRepository.ArtistRepository
import com.example.quotes.data.repository.albumRepository.TopAlbumRepository
import com.example.quotes.domain.usecase.DetailUseCase
import com.example.quotes.domain.usecase.HistoryUseCase
import com.example.quotes.domain.usecase.SearchUseCase
import com.example.quotes.domain.usecase.TopAlbumUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideQuotableServerRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }


    /**
     *
     * @param retrofit must ber quotable server Retrofit
     * @return RandomQuoteApi
     */
    @Provides
    @Singleton
    fun provideAlbumService(retrofit: Retrofit): LastFMService {
        return retrofit.create(LastFMService::class.java)
    }

    /**
     *
     * @param randomQuoteApi RandomQuoteApi
     * @return RandomQuoteRepository
     */
    @Provides
    @Singleton
    fun provideArtistRepository(
        randomQuoteApi: LastFMService,
        lastFMMapper: LastFMResponseMapper
    ): ArtistRepository {
        return ArtistRepository(randomQuoteApi, lastFMMapper)
    }

    /**
     *
     * @param provideTopAlbumRepository RandomQuoteApi
     * @return RandomQuoteRepository
     */
    @Provides
    @Singleton
    fun provideTopAlbumRepository(
        randomQuoteApi: LastFMService,
        lastFMMapper: TopAlbumResponseMapper
    ): TopAlbumRepository {
        return TopAlbumRepository(randomQuoteApi, lastFMMapper)
    }

    @Provides
    @Singleton
    fun provideAlbumDetailRepository(
        randomQuoteApi: LastFMService,
        LastFMDao: LastFMDao,
        albumDetailMapper: AlbumDetailResponseMapper,
        albumDetailModelMapper: AlbumDetailModelMapper
    ): AlbumDetailRepository {
        return AlbumDetailRepository(
            randomQuoteApi,
            LastFMDao,
            albumDetailMapper,
            albumDetailModelMapper
        )
    }

    @Provides
    @Singleton
    fun provideAlbumRepository(
        LastFMDao: LastFMDao,
        albumMapper: AlbumResponseMapper
    ): AlbumRepository {
        return AlbumRepository(LastFMDao, albumMapper)
    }

    /**
     *
     * @param randomQuoteRepository RandomQuoteRepository
     * @return MainShowCase
     */
    @Provides
    @Singleton
    fun provideTopAlbumUseCase(
        topAlbumRepository: TopAlbumRepository,
    ): TopAlbumUseCase {
        return TopAlbumUseCase(
            topAlbumRepository
        )
    }

    @Provides
    @Singleton
    fun provideDetailUseCase(
        albumDetailRepository: AlbumDetailRepository,
        albumRepository: AlbumRepository
    ): DetailUseCase {
        return DetailUseCase(albumRepository, albumDetailRepository)
    }

    @Provides
    @Singleton
    fun provideHistoryUseCase(
        albumRepository: AlbumRepository
    ): HistoryUseCase {
        return HistoryUseCase(albumRepository)
    }

    @Provides
    @Singleton
    fun provideSearchUseCase(
        artistRepository: ArtistRepository
    ): SearchUseCase {
        return SearchUseCase(artistRepository)
    }
}