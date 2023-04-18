package com.example.ofiu.usecases.navigation

sealed class AppScreens (val route: String){
    object SplashScreen: AppScreens("splash_screen")
    object Session: AppScreens("session")
}