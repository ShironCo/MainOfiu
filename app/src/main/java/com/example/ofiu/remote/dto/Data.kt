package com.example.ofiu.remote.dto


import com.squareup.moshi.Json

data class Data(
    @field: Json(name = "comentarios")
    val comentarios: String,
    @field: Json(name = "estrellas")
    val estrellas: String,
    @field: Json(name = "id_profesional")
    val idProfesional: String
)