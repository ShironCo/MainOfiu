package com.example.ofiu.usecases.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ofiu.usecases.clientUser.BottonScreen
import com.example.ofiu.usecases.clientUser.chat.ChatApp
import com.example.ofiu.usecases.clientUser.home.HomeApp
import com.example.ofiu.usecases.clientUser.profile.ProfileApp
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
import com.example.ofiu.usecases.session.register.RegisterViewModel

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
        composable(AppScreens.Legal.route){
            LegalApp(navController, RegisterViewModel())
        }
        composable(AppScreens.Register.route){
            RegisterApp(navController, RegisterViewModel())
        }
        composable(AppScreens.ForgotPassword.route){
            ForgotPassword(navController, ForgotPasswordViewModel())
        }
        composable(AppScreens.ForgotPasswordTwo.route+"/{email}"){
            val email = it.arguments?.getString("email")
            ForgotPasswordTwo(navController, ForgotPasswordViewModel(), email)
        }
        composable(AppScreens.ForgotPasswordThree.route+"/{email}"){
            val email = it.arguments?.getString("email")
            ForgotPasswordThree(navController, ForgotPasswordViewModel(), email)
        }
        composable(AppScreens.BottomBarScreen.route){
            BottonScreen()
        }
    }
}