package com.example.ofiu.remote.dto.ofiu.professionals


import com.squareup.moshi.Json

data class User(
    @field: Json(name = "apellido")
    val apellido: String,
    @field: Json(name = "desc_profesional")
    val descProfesional: String,
    @field: Json(name = "estrellas")
    val estrellas: String,
    @field: Json(name = "etiqueta")
    val etiqueta: String,
    @field: Json(name = "id_profesional")
    val idProfesional: String,
    @field: Json(name = "img_perfil")
    val imgPerfil: String,
    @field: Json(name = "nombre")
    val nombre: String
)