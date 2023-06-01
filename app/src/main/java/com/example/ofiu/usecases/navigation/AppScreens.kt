package com.example.ofiu.usecases.navigation

sealed class AppScreens (val route: String){
    object SplashScreen: AppScreens("splash_screen")
    object Session: AppScreens("session")
    object Login: AppScreens("login")
    object Register: AppScreens("register")
    object Legal: AppScreens("legal")
    object ForgotPassword: AppScreens("forgot_password")
    object ForgotPasswordTwo: AppScreens("forgot_passwordTwo")
    object ForgotPasswordThree: AppScreens("forgot_passwordThree")
    object BottomBarScreen: AppScreens("bottom_barScreen")
    object DrawerScreen: AppScreens("drawer_screen")
    object Menu: AppScreens("menu")
    object VerifyId: AppScreens("verify_id")
    object VerifyFace: AppScreens("verify_face")
}