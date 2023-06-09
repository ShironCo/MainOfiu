package com.example.aprendiendoausargpt.screen.data.remote.dto


import com.squareup.moshi.Json

data class Message(
    @field: Json(name = "content")
    val content: String,
    @field: Json(name = "role")
    val role: String
)