package com.example.ofiu.domain

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.example.ofiu.remote.dto.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface OfiuRepository {
    suspend fun addUser(user: RegisterUserRequest): Result<UserResponse>
    suspend fun loginUser(user: LoginUserRequest): Result<LoginResponse>
    suspend fun changeUser(user: UserRequest): Result<UserResponse>

    suspend fun sendImage(
        image1: MultipartBody.Part,
        image2: MultipartBody.Part,
        image3: MultipartBody.Part,
        id: RequestBody
    ): Result<UserResponse>

    //Repository Camera
    suspend fun captureImage(): ImageCapture
    suspend fun showCameraPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        cameraFacing: Boolean
    )
}

