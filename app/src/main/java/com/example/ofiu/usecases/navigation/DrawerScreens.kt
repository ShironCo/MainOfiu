package com.example.ofiu.usecases.navigation

import com.example.ofiu.R

sealed class DrawerScreens (
    val route: String,
    val title: String,
    val img: Int
        ) {
    object Profile: DrawerScreens(
        route = "profile",
        title = "Perfil profesional",
        img = R.drawable.baseline_person_24
    )
    object Chat: DrawerScreens(
        route = "chat",
        title = "Chat",
        img = R.drawable.baseline_chat_24
    )
    object VerifyId: DrawerScreens(
        route = "verify",
        title = "Verificación de identidad",
        img = R.drawable.baseline_verified_user_24
    )
}