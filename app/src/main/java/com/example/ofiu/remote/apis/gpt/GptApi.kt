package com.example.ofiu.remote.apis.gpt

import com.example.aprendiendoausargpt.screen.data.remote.dto.GptRequestDto
import com.example.aprendiendoausargpt.screen.data.remote.dto.GptResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatGptApi {

    // Define una constante BASE_URL que representa la URL base de la API
    companion object {
        const val BASE_URL = "https://api.openai.com/v1/"
    }

    // Realiza una solicitud POST a la ruta "completions" de la API con el cuerpo de la solicitud especificado
    // y devuelve una respuesta de tipo GptResponseDto
    @POST("completions")
    suspend fun getInformation(@Body body: GptRequestDto):
            GptResponseDto

}