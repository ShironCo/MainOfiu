package com.example.ofiu.remote.repository

import com.example.ofiu.domain.OfiuRepository
import com.example.ofiu.remote.OfiuApi
import com.example.ofiu.remote.dto.RegisterUserRequest
import com.example.ofiu.remote.dto.RegisterUserResponse

class OfiuRepositoryImpl(
    private val api: OfiuApi
): OfiuRepository {
    override suspend fun addUser(user: RegisterUserRequest): Result<RegisterUserResponse> {
        return try {
            val response = api.addUser(user)
            Result.success(response)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}