package com.example.ofiu.remote

import com.example.ofiu.remote.dto.RegisterUserRequest
import com.example.ofiu.remote.dto.RegisterUserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface OfiuApi {
    companion object{
        const val BASE_URL = "https://ofiu.000webhostapp.com/ofiu2023/"
    }

   @POST("reg_users.php")
    suspend fun addUser(
       @Body registerUser: RegisterUserRequest
    ): RegisterUserResponse
}