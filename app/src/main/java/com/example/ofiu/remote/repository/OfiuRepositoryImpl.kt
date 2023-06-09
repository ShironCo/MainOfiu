package com.example.ofiu.remote.repository


import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.example.aprendiendoausargpt.screen.data.remote.dto.GptRequestDto
import com.example.aprendiendoausargpt.screen.data.remote.dto.GptResponseDto
import com.example.ofiu.domain.OfiuRepository
import com.example.ofiu.remote.apis.gpt.ChatGptApi
import com.example.ofiu.remote.apis.ofiu.OfiuApi
import com.example.ofiu.remote.dto.*
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part
import okhttp3.RequestBody
import javax.inject.Inject

class OfiuRepositoryImpl @Inject constructor(
    private val gpt: ChatGptApi,
    private val api: OfiuApi,
    private val cameraProvider: ProcessCameraProvider,
    private val preview: Preview,
    private val imageAnalysis: ImageAnalysis,
    private val imageCapture: ImageCapture
) : OfiuRepository {
    override suspend fun addUser(user: RegisterUserRequest): Result<UserResponse> {
        return try {
            val response = api.addUser(user)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginUser(user: LoginUserRequest): Result<LoginResponse> {
        return try {
            val response = api.loginUser(user)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun changeUser(user: UserRequest): Result<UserResponse> {
        return try {
            val response = api.changeUser(user)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendImage(
        image1: Part,
        image2: Part,
        image3: Part,
        id: RequestBody
    ): Result<UserResponse> {
        return try {
            Result.success(api.sendImage(
                image1, image2, image3, id
            ))
        } catch (e: Exception) {
            Result.success(UserResponse(e.toString()))
        }
    }

    override suspend fun sendImageGallery(
        id: RequestBody,
        images: Array<MultipartBody.Part>
    ): Result<UserResponse> {
        return try {
            Result.success(api.sendImageGallery(
                id,
                images
            ))
        } catch (e: Exception) {
            Result.success(UserResponse(e.toString()))
        }
    }


    //Camera functions

    override suspend fun captureImage(): ImageCapture {
        return imageCapture
    }

    override suspend fun showCameraPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        cameraFacing: Boolean
    ) {
        preview.setSurfaceProvider(previewView.surfaceProvider)
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.Builder().requireLensFacing(
                    if (cameraFacing) {
                        CameraSelector.LENS_FACING_BACK
                    } else {
                        CameraSelector.LENS_FACING_FRONT
                    }
                ).build(),
                preview,
                imageAnalysis,
                imageCapture
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun promptGpt(prompt: String): Result<GptResponseDto> {
        return try {
            val text = gpt.getInformation(GptRequestDto(prompt = "Crea una descripcion profesional de minimo 25 palabras y maximo 30 palabras con las siguientes palabras clave: $prompt"))
            Result.success(text)
        }catch(e: Exception){
            Result.failure(e)
        }
    }

}



