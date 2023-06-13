package com.example.ofiu.remote.dto.ofiu.professionals.details.comments


import com.squareup.moshi.Json

data class Comments(
    @field: Json(name = "comentarios")
    val comentarios: List<Comentario>
)