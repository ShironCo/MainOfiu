package com.example.ofiu.domain

import com.example.ofiu.remote.dto.RegisterUserRequest
import com.example.ofiu.remote.dto.RegisterUserResponse

interface OfiuRepository {
    suspend fun addUser(user: RegisterUserRequest): Result<RegisterUserResponse>
}