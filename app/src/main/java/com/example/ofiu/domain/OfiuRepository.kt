package com.example.ofiu.domain

import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.example.aprendiendoausargpt.screen.data.remote.dto.GptResponseDto
import com.example.ofiu.remote.dto.gpt.UserProRequest
import com.example.ofiu.remote.dto.ofiu.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface OfiuRepository {
    suspend fun addUser(user: RegisterUserRequest): Result<UserResponse>
    suspend fun loginUser(user: LoginUserRequest): Result<LoginResponse>
    suspend fun changeUser(user: UserRequest): Result<UserResponse>
    suspend fun receiveImagesPro(id: UserProRequest): Result<ImageProfileGallery>
    suspend fun recoverPassword(email: String): Result<UserResponse>
    suspend fun deleteImageGallery(uri: String): Result<UserResponse>
    suspend fun sendDescrPro(desc: UserRequest): Result<UserResponse>
    suspend fun updatePhotoProfile(
        id: RequestBody,
        image: MultipartBody.Part
    ): Result<UserResponse>

    suspend fun sendImage(
        image1: MultipartBody.Part,
        image2: MultipartBody.Part,
        image3: MultipartBody.Part,
        id: RequestBody
    ): Result<UserResponse>

    suspend fun sendImageGallery(
        id:RequestBody,
        images: Array<MultipartBody.Part>
    ): Result<UserResponse>

    //Repository Camera
    suspend fun captureImage(): ImageCapture
    suspend fun showCameraPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        cameraFacing: Boolean
    )

    suspend fun promptGpt(prompt: String): Result<GptResponseDto>
}

