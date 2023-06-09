package com.example.aprendiendoausargpt.screen.data.remote.dto


import com.squareup.moshi.Json

data class GptRequestDto(
    @field: Json(name = "max_tokens")
    val maxTokens: Int = 250,
    @field: Json(name = "model")
    val model: String = "text-davinci-003",
    @field: Json(name = "prompt")
    val prompt: String,
    @field: Json(name = "temperature")
    val temperature: Double = 0.0,
)