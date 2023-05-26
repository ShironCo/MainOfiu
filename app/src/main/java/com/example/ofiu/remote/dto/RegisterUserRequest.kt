package com.example.ofiu.remote.dto

data class RegisterUserRequest(
    val name: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val password: String
)
