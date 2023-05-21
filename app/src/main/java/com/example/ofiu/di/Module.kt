package com.example.ofiu.di

import com.example.ofiu.domain.OfiuRepository
import com.example.ofiu.remote.OfiuApi
import com.example.ofiu.remote.repository.OfiuRepositoryImpl
import dagger.hilt.InstallIn
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object Module {

    @Provides
    @Singleton
    fun provideOfiuApi(): OfiuApi{
        return Retrofit.Builder()
            .baseUrl(OfiuApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .client(
                OkHttpClient.Builder().build()
            ).build().create(OfiuApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: OfiuApi): OfiuRepository {
        return OfiuRepositoryImpl(api)
    }

}