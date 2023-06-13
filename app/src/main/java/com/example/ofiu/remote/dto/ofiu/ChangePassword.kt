package com.example.ofiu.remote.dto.ofiu

data class ChangePassword(
    val email: String,
    val password: String,
    val passwordRepeat: String
)
