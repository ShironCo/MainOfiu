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
import com.example.ofiu.remote.dto.gpt.UserProRequest
import com.example.ofiu.remote.dto.ofiu.*
import com.example.ofiu.remote.dto.ofiu.professionals.DataRecycleView
import com.example.ofiu.remote.dto.ofiu.professionals.details.DetailsPro
import com.example.ofiu.remote.dto.ofiu.professionals.details.comments.Comments
import com.example.ofiu.remote.dto.ofiu.professionals.details.comments.RequestComment
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

    override suspend fun receiveImagesPro(id: UserProRequest): Result<ImageProfileGallery> {
        return try {
            val response = api.receiveImagesPro(id)
            Result.success(response)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun recoverPassword(email: String): Result<UserResponse> {
        return try{
            Result.success(api.recoverPassword(Request(email)))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun deleteImageGallery(uri: String): Result<UserResponse> {
        return try {
            Result.success(api.deletePhotoGallery(Request(uri)))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun sendDescrPro(desc: UserRequest): Result<UserResponse> {
        return try {
            Result.success(api.sendDescrPro(desc))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun getUsersPro(id: String, search: String): Result<DataRecycleView> {
        return try {
            Result.success(api.getUsersPro(UserRequest(id, search)))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun getDetailsPro(id: String): Result<DetailsPro> {
        return try {
            Result.success(api.getDetailsPro(Request(id)))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun getCommentsPro(id: String): Result<Comments> {
        return try {
            Result.success(api.getCommentsPro(Request(id)))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun setCommentsPro(
        idPro: String,
        idUser: String,
        desc: String,
        starts: String
    ): Result<UserResponse> {
        return try {
            Result.success(api.setCommentsPro(RequestComment(idPro, idUser, desc, starts)))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun updatePassword(
        email: String,
        password: String,
        passwordRepeat: String
    ): Result<UserResponse> {
        return try {
            Result.success(api.updatePassword(
                 ChangePassword(
                     email,
                     password,
                     passwordRepeat
                 )
            ))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun sendCode(email: String, code: String): Result<UserResponse> {
        return try {
            Result.success(api.sendCode(UserRequest(email, code)))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun reportUser(
        idprof: String,
        iduser: String,
        deta: String
    ): Result<UserResponse> {
        return try {
            Result.success(api.reportUser(ReportUser(
                idprof, iduser, deta
            )))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun updatePhotoProfile(id: RequestBody, image: Part): Result<UserResponse> {
        return try {
            Result.success(api.updatePhotoProfile(id, image))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun sendImage(id: RequestBody, photo: Array<Part>): Result<UserResponse> {
        return try {
            Result.success(api.sendImage(
                id,
                photo
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

    override suspend fun getTags(prompt: String): Result<GptResponseDto> {
        return try {
            val text = gpt.getInformation(GptRequestDto(prompt = "Crea una sola etiqueta que mejor identifique la siguiente descripcion profesional, tiene que tener como maximo una frase de 4 palabras completas, no escribas \"Etiqueta:\" : $prompt"))
            Result.success(text)
        }catch(e: Exception){
            Result.failure(e)
        }
    }
}



