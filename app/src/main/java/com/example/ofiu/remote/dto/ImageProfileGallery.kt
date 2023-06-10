package com.example.ofiu.remote.dto


import com.squareup.moshi.Json

data class ImageProfileGallery(
    @field: Json(name = "data")
    val `data`: Data,
    @field: Json(name = "data1")
    val data1: List<String>
)