package com.example.ofiu.domain

import com.example.ofiu.remote.dto.LoginResponse
import com.example.ofiu.remote.dto.LoginUserRequest
import com.example.ofiu.remote.dto.RegisterUserRequest
import com.example.ofiu.remote.dto.UserResponse

interface OfiuRepository {
    suspend fun addUser(user: RegisterUserRequest): Result<UserResponse>
    suspend fun loginUser(user: LoginUserRequest) : Result<LoginResponse>
}