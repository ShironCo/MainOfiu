package com.example.ofiu.remote.dto.ofiu.professionals.details.comments


import com.squareup.moshi.Json

data class Comentario(
    @field: Json(name = "apellido")
    val apellido: String,
    @field: Json(name = "comentario")
    val comentario: String,
    @field: Json(name = "estrella")
    val estrella: String,
    @field: Json(name = "fechadecomentario")
    val fechadecomentario: String,
    @field: Json(name = "img_perfil")
    val imgPerfil: String,
    @field: Json(name = "nombre")
    val nombre: String
)