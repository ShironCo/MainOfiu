package com.example.ofiu.remote.dto.ofiu.professionals.details


import com.squareup.moshi.Json

data class Informacion(
    @field: Json(name = "apellido")
    val apellido: String,
    @field: Json(name = "comentarios")
    val comentarios: String,
    @field: Json(name = "desc_profesional")
    val descProfesional: String,
    @field: Json(name = "estrellas")
    val estrellas: String,
    @field: Json(name = "img_1")
    val img1: String,
    @field: Json(name = "img_perfil")
    val imgPerfil: String,
    @field: Json(name = "nombre")
    val nombre: String
)