package com.example.ofiu.remote.apis.gpt

import com.example.aprendiendoausargpt.screen.data.remote.dto.GptRequestDto
import com.example.aprendiendoausargpt.screen.data.remote.dto.GptResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatGptApi {

    companion object{
        const val BASE_URL = "https://api.openai.com/v1/"
    }

    @POST("completions")
    suspend fun getInformation(@Body body: GptRequestDto):
            GptResponseDto

}