package com.example.ofiu.di

import android.app.Application
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import com.example.ofiu.domain.OfiuRepository
import com.example.ofiu.remote.apis.gpt.ChatGptApi
import com.example.ofiu.remote.apis.gpt.ApiKeyInterceptor
import com.example.ofiu.remote.apis.ofiu.OfiuApi
import com.example.ofiu.remote.repository.OfiuRepositoryImpl
import dagger.hilt.InstallIn
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object Module {

    @Provides
    @Singleton
    fun provideOfiuApi(): OfiuApi {
        return Retrofit.Builder()
            .baseUrl(OfiuApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .client(
                OkHttpClient.Builder().build()
            ).build().create(OfiuApi::class.java)
    }

    @Provides
    @Singleton
    fun provideApi(): ChatGptApi {
        return Retrofit.Builder().baseUrl(ChatGptApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor()).addInterceptor(ApiKeyInterceptor())
                .build()).build().create()
    }


    @Provides
    @Singleton
    fun provideRepository(
        gpt: ChatGptApi,
        api: OfiuApi,
        cameraProvider: ProcessCameraProvider,
        imageCapture: ImageCapture,
        imageAnalysis: ImageAnalysis,
        preview: Preview
    ): OfiuRepository {
        return OfiuRepositoryImpl(
            gpt,
            api,
            cameraProvider,
            preview,
            imageAnalysis,
            imageCapture
        )
    }


    @Provides
    @Singleton
    fun provideCameraProvider(application: Application): ProcessCameraProvider {
        return ProcessCameraProvider.getInstance(application).get()
    }

    @Provides
    @Singleton
    fun provideCameraPreview(): Preview {
        return Preview.Builder().build()
    }

    @Provides
    @Singleton
    fun provideImageCapture(): ImageCapture {
        return ImageCapture.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideImageAnalysis(): ImageAnalysis {
        return ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }

}