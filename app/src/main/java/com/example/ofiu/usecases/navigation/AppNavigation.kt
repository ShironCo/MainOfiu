package com.example.ofiu.usecases.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ofiu.usecases.splash.SplashScreen
import com.example.ofiu.usecases.session.SessionApp
import com.example.ofiu.usecases.session.login.LoginApp
import com.example.ofiu.usecases.session.login.LoginViewModel
import com.example.ofiu.usecases.session.register.RegisterApp

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.route){
        composable(AppScreens.SplashScreen.route){
            SplashScreen(navController)
        }
        composable(AppScreens.Session.route){
            SessionApp(navController)
        }
        composable(AppScreens.Login.route){
            LoginApp(navController, LoginViewModel())
        }
        composable(AppScreens.Register.route){
            RegisterApp()
        }
    }
}