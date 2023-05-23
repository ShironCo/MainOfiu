package com.example.ofiu.usecases.navigation

import com.example.ofiu.R

sealed class BottomBarScreen (
    val route: String,
    val title: String,
    val Icon: Int
    ){

    object Home: BottomBarScreen(
        route = "home",
        title = "Home",
        Icon = R.drawable.baseline_home_24
    )
    object Chat: BottomBarScreen(
        route = "chat",
        title = "Chat",
        Icon = R.drawable.baseline_chat_24
    )
    object Profile: BottomBarScreen(
        route = "profile",
        title = "Profile",
        Icon = R.drawable.baseline_person_24
    )

}