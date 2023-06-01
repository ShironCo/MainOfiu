package com.example.ofiu.di

import android.app.Application
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
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
    fun provideRepository(
        api: OfiuApi,
        cameraProvider: ProcessCameraProvider,
        selector: CameraSelector,
        imageCapture: ImageCapture,
        imageAnalysis: ImageAnalysis,
        preview: Preview
    ): OfiuRepository {
        return OfiuRepositoryImpl(
            api,
            cameraProvider,
            selector,
            preview,
            imageAnalysis,
            imageCapture
        )
    }

    //Provides Camera

    @Provides
    @Singleton
    fun provideCameraSelector(): CameraSelector {
        return CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
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