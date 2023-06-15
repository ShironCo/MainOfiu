package com.example.ofiu.remote.apis.ofiu

import com.example.ofiu.remote.dto.*
import com.example.ofiu.remote.dto.gpt.UserProRequest
import com.example.ofiu.remote.dto.ofiu.*
import com.example.ofiu.remote.dto.ofiu.professionals.DataRecycleView
import com.example.ofiu.remote.dto.ofiu.professionals.details.DetailsPro
import com.example.ofiu.remote.dto.ofiu.professionals.details.comments.Comments
import com.example.ofiu.remote.dto.ofiu.professionals.details.comments.RequestComment
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
    @POST("eliminar_foto_galeria.php")
    suspend fun deletePhotoGallery(
       @Body image: Request
    ): UserResponse
    @POST("update_descr.php")
    suspend fun sendDescrPro(
       @Body desc: UserRequest
    ): UserResponse

    @Multipart
    @POST("update_foto_perfil.php")
    suspend fun updatePhotoProfile(
       @Part ("id") id : RequestBody,
       @Part photo: MultipartBody.Part
    ): UserResponse


    @POST("format_correo_reset.php")
    suspend fun recoverPassword(@Body email: Request): UserResponse
    @Multipart
    @POST("verficar_usuario1.php")
    suspend fun sendImage(
        @Part ("id") id: RequestBody,
        @Part photo : Array<MultipartBody.Part>
    ): UserResponse

    @Multipart
    @POST("subir_galeria.php")
    suspend fun sendImageGallery(
        @Part ("id") id: RequestBody,
        @Part photo : Array<MultipartBody.Part>
    ): UserResponse

    @POST("img_com_est.php")
    suspend fun receiveImagesPro(
        @Body loginUser: UserProRequest
    ): ImageProfileGallery
    @POST("pag_prueba.php")
    suspend fun getUsersPro(
        @Body ids: UserRequest
    ): DataRecycleView

    @POST("select_ver_perfil.php")
    suspend fun getDetailsPro(
        @Body id: Request
    ): DetailsPro
    @POST("select_comen_prof.php")
    suspend fun getCommentsPro(
        @Body id: Request
    ): Comments
      @POST("insert_coment_prof.php")
    suspend fun setCommentsPro(
        @Body id: RequestComment
    ): UserResponse
    @POST("cambiar_contraseña_reset.php")
    suspend fun updatePassword(
        @Body newsPasswords: ChangePassword
    ): UserResponse
  @POST("verificar_codigo_reset.php")
    suspend fun sendCode(
        @Body sendCode: UserRequest
    ): UserResponse
  @POST("reportes_profe.php")
    suspend fun reportUser(
        @Body sendCode: ReportUser
    ): UserResponse

}