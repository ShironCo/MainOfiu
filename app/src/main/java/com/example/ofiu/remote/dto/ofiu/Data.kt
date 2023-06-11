package com.example.ofiu.remote.dto.ofiu


import com.squareup.moshi.Json

data class Data(
    @field: Json(name = "comentarios")
    val comentarios: Int? = 0,
    @field: Json(name = "estrellas")
    val estrellas: Int? = 0,
    @field: Json(name = "id_profesional")
    val idProfesional: String,
    @field: Json(name = "img_perfil")
    val image: String,
    @field: Json(name = "desc_profesional")
    val desc: String
)