package com.example.ofiu.remote.apis.ofiu

import com.example.ofiu.remote.dto.*
import com.example.ofiu.remote.dto.gpt.UserProRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface OfiuApi {
    companion object{
        const val BASE_URL = "https://ofiu.online/kotlin/"
    }

   @POST("reg_users.php")
    suspend fun addUser(
       @Body registerUser: RegisterUserRequest
    ): UserResponse

    @POST("tipo_user.php")
    suspend fun changeUser(
        @Body userRequest: UserRequest
    ): UserResponse

    @POST("ini_sesion.php")
    suspend fun loginUser(
       @Body loginUser: LoginUserRequest
    ): LoginResponse

    @POST("format_correo_reset.php")
    suspend fun recoverPassword(@Body email: Request): UserResponse
    @Multipart
    @POST("verificado_usuario.php")
    suspend fun sendImage(
        @Part image1: MultipartBody.Part,
        @Part image2: MultipartBody.Part,
        @Part image3: MultipartBody.Part,
        @Part ("id") id: RequestBody
    ): UserResponse

    @Multipart
    @POST("subir_galeria.php")
   // @POST("subirImage.php")
    suspend fun sendImageGallery(
        @Part ("id") id: RequestBody,
        @Part photo : Array<MultipartBody.Part>
    ): UserResponse

    @POST("img_com_est.php")
    suspend fun receiveImagesPro(
        @Body loginUser: UserProRequest
    ): ImageProfileGallery


}