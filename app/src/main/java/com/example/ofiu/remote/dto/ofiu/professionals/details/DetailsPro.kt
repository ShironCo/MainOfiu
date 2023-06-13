package com.example.ofiu.remote.dto.ofiu.professionals.details


import com.squareup.moshi.Json

data class DetailsPro(
    @field: Json(name = "imagenes")
    val imagenes: List<String>,
    @field: Json(name = "informacion")
    val informacion: Informacion
)