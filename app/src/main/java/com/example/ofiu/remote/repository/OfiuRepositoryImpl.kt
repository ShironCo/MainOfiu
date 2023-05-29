package com.example.ofiu.remote.repository

import com.example.ofiu.domain.OfiuRepository
import com.example.ofiu.remote.OfiuApi
import com.example.ofiu.remote.dto.LoginResponse
import com.example.ofiu.remote.dto.LoginUserRequest
import com.example.ofiu.remote.dto.RegisterUserRequest
import com.example.ofiu.remote.dto.UserResponse

class OfiuRepositoryImpl(
    private val api: OfiuApi
): OfiuRepository {
    override suspend fun addUser(user: RegisterUserRequest): Result<UserResponse> {
        return try {
            val response = api.addUser(user)
            Result.success(response)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun loginUser(user: LoginUserRequest): Result<LoginResponse> {
        return try {
            val response = api.loginUser(user)
            Result.success(response)
        }catch (e: Exception){
            Result.failure(e)
        }
        }
    }