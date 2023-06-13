package com.example.ofiu.remote.dto.ofiu.professionals.details.comments

data class RequestComment(
    val idPro: String,
    val idUser: String,
    val desc: String,
    val starts: String
)
