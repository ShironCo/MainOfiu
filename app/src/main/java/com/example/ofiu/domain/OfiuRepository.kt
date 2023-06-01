package com.example.ofiu.domain

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.example.ofiu.remote.dto.LoginResponse
import com.example.ofiu.remote.dto.LoginUserRequest
import com.example.ofiu.remote.dto.RegisterUserRequest
import com.example.ofiu.remote.dto.UserResponse

interface OfiuRepository {
    suspend fun addUser(user: RegisterUserRequest): Result<UserResponse>
    suspend fun loginUser(user: LoginUserRequest) : Result<LoginResponse>


    //Repository Camera
    suspend fun captureImage(): ImageCapture
    suspend fun showCameraPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    )
}

