package com.example.ofiu.domain

import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.example.aprendiendoausargpt.screen.data.remote.dto.GptResponseDto
import com.example.ofiu.remote.dto.gpt.UserProRequest
import com.example.ofiu.remote.dto.ofiu.*
import com.example.ofiu.remote.dto.ofiu.professionals.DataRecycleView
import com.example.ofiu.remote.dto.ofiu.professionals.User
import com.example.ofiu.remote.dto.ofiu.professionals.details.DetailsPro
import com.example.ofiu.remote.dto.ofiu.professionals.details.comments.Comments
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

interface OfiuRepository {

    // Funciones relacionadas con usuarios
    suspend fun addUser(user: RegisterUserRequest): Result<UserResponse>
    suspend fun loginUser(user: LoginUserRequest): Result<LoginResponse>
    suspend fun changeUser(user: UserRequest): Result<UserResponse>
    suspend fun receiveImagesPro(id: UserProRequest): Result<ImageProfileGallery>
    suspend fun recoverPassword(email: String): Result<UserResponse>
    suspend fun deleteImageGallery(uri: String): Result<UserResponse>
    suspend fun sendDescrPro(desc: UserRequest): Result<UserResponse>
    suspend fun getUsersPro(id: String, search: String): Result<DataRecycleView>
    suspend fun getDetailsPro(id: String): Result<DetailsPro>
    suspend fun getCommentsPro(id: String): Result<Comments>
    suspend fun setCommentsPro(idPro: String, idUser: String, desc: String, starts: String): Result<UserResponse>
    suspend fun updatePassword(email: String, password: String, passwordRepeat: String): Result<UserResponse>
    suspend fun sendCode(email: String, code: String): Result<UserResponse>
    suspend fun reportUser(idprof: String, iduser: String, deta:String): Result<UserResponse>

    // Funciones relacionadas con la galería de imágenes

    suspend fun updatePhotoProfile(
        id: RequestBody,
        image: MultipartBody.Part
    ): Result<UserResponse>

    suspend fun sendImage(
        id: RequestBody,
        photo : Array<MultipartBody.Part>
    ): Result<UserResponse>

    suspend fun sendImageGallery(
        id:RequestBody,
        images: Array<MultipartBody.Part>
    ): Result<UserResponse>

    // Funciones relacionadas con la cámara

    suspend fun captureImage(): ImageCapture
    suspend fun showCameraPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        cameraFacing: Boolean
    )

    // Funciones relacionadas con la comunicación con GPT

    suspend fun promptGpt(prompt: String): Result<GptResponseDto>
    suspend fun getTags(prompt: String): Result<GptResponseDto>
}

