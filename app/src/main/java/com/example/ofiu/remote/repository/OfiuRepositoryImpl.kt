package com.example.ofiu.remote.repository


import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.example.ofiu.domain.OfiuRepository
import com.example.ofiu.remote.OfiuApi
import com.example.ofiu.remote.dto.LoginResponse
import com.example.ofiu.remote.dto.LoginUserRequest
import com.example.ofiu.remote.dto.RegisterUserRequest
import com.example.ofiu.remote.dto.UserResponse
import javax.inject.Inject

class OfiuRepositoryImpl @Inject constructor(
    private val api: OfiuApi,
    private val cameraProvider: ProcessCameraProvider,
    private val selector: CameraSelector,
    private val preview: Preview,
    private val imageAnalysis: ImageAnalysis,
    private val imageCapture: ImageCapture
): OfiuRepository {
    override suspend fun addUser(user: RegisterUserRequest): Result<UserResponse> {
        return try {
            val response = api.addUser(user)
            Result.success(response)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun loginUser(user: LoginUserRequest): Result<LoginResponse> {
        return try {
            val response = api.loginUser(user)
            Result.success(response)
        }catch (e: Exception){
            Result.failure(e)
        }
        }


    //Camera functions

    override suspend fun captureImage(): ImageCapture {
        return imageCapture
    }

    override suspend fun showCameraPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) {
        preview.setSurfaceProvider(previewView.surfaceProvider)
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                selector,
                preview,
                imageAnalysis,
                imageCapture
            )
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

}



