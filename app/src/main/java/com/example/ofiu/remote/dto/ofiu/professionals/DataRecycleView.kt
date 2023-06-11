package com.example.ofiu.remote.dto.ofiu.professionals


import com.squareup.moshi.Json

data class DataRecycleView(
    @field: Json(name = "users")
    val users: List<User>
)