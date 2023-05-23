package com.example.ofiu.usecases.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ofiu.usecases.clientUser.BottonScreen
import com.example.ofiu.usecases.splash.SplashScreen
import com.example.ofiu.usecases.session.SessionApp
import com.example.ofiu.usecases.session.login.forgotPassword.ForgotPassword
import com.example.ofiu.usecases.session.login.forgotPassword.ForgotPasswordThree
import com.example.ofiu.usecases.session.login.forgotPassword.ForgotPasswordTwo
import com.example.ofiu.usecases.session.login.forgotPassword.ForgotPasswordViewModel
import com.example.ofiu.usecases.session.login.LoginApp
import com.example.ofiu.usecases.session.login.LoginViewModel
import com.example.ofiu.usecases.session.register.LegalApp
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
            LoginApp(navController)
        }
        composable(AppScreens.Legal.route){
            LegalApp(navController)
        }
        composable(AppScreens.Register.route){
            RegisterApp(navController)
        }
        composable(AppScreens.ForgotPassword.route){
            ForgotPassword(navController)
        }
        composable(AppScreens.ForgotPasswordTwo.route+"/{email}"){
            val email = it.arguments?.getString("email")
            ForgotPasswordTwo(navController, email)
        }
        composable(AppScreens.ForgotPasswordThree.route+"/{email}"){
            val email = it.arguments?.getString("email")
            ForgotPasswordThree(navController, email)
        }
        composable(AppScreens.BottomBarScreen.route){
            BottonScreen()
        }
    }
}