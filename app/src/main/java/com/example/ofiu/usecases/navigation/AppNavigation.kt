package com.example.ofiu.usecases.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ofiu.usecases.menú.MenuApp
import com.example.ofiu.usecases.splash.SplashScreen
import com.example.ofiu.usecases.session.SessionApp
import com.example.ofiu.usecases.session.login.forgotPassword.ForgotPassword
import com.example.ofiu.usecases.session.login.forgotPassword.ForgotPasswordThree
import com.example.ofiu.usecases.session.login.forgotPassword.ForgotPasswordTwo
import com.example.ofiu.usecases.session.login.LoginApp
import com.example.ofiu.usecases.session.register.legal.LegalApp
import com.example.ofiu.usecases.session.register.RegisterApp
import com.example.ofiu.usecases.session.register.legal.TermAndConditionsApp
import com.example.ofiu.usecases.users.clientUser.BottomScreen
import com.example.ofiu.usecases.users.clientUser.chat.ChatApp
import com.example.ofiu.usecases.users.clientUser.chat.messages.MessageApp
import com.example.ofiu.usecases.users.clientUser.home.details.DetailsUserApp
import com.example.ofiu.usecases.users.clientUser.home.details.reporting.ReportApp
import com.example.ofiu.usecases.users.workerUser.DrawerScreen
import com.example.ofiu.usecases.users.workerUser.verifyId.VerifyFaceApp
import com.example.ofiu.usecases.users.workerUser.verifyId.VerifyIdApp
import com.example.ofiu.usecases.users.workerUser.verifyId.VerifyWorkerApp


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
        composable(AppScreens.Term.route){
            TermAndConditionsApp(navController)
        }
        composable(AppScreens.Policy.route){
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
        composable(AppScreens.Menu.route){
            MenuApp(navController)
        }
        composable(AppScreens.BottomBarScreen.route){
            BottomScreen(navController)
        }
        composable(AppScreens.DrawerScreen.route){
            DrawerScreen(navController)
        }

        //Verify
        composable(DrawerScreens.VerifyId.route){
            VerifyWorkerApp(navController)
        }
        composable(AppScreens.VerifyId.route){
            VerifyIdApp(navController)
        }
        composable(AppScreens.VerifyFace.route){
            VerifyFaceApp(navController)
        }

        //ClientMenu
        composable(AppScreens.DetailsPro.route+"/{id}"){
            val id = it.arguments?.getString("id")
            id?.let {
                DetailsUserApp(id, navController)
            }
        }

        composable(AppScreens.Report.route+"/{id}"){
            ReportApp(navController, it.arguments?.getString("id"))
        }

        composable(AppScreens.Chat.route+"/{backtopbar}"){
            ChatApp(navController, it.arguments?.getString("backtopbar"))
        }

        composable(AppScreens.Messages.route+"/{idUser}/{idPro}/{name}/{image}/{idSender}"){
            MessageApp(
                navController,
                it.arguments?.getString("idUser"),
                it.arguments?.getString("idPro"),
                it.arguments?.getString("name"),
                it.arguments?.getString("image"),
                it.arguments?.getString("idSender"),
            )
        }
    }
}