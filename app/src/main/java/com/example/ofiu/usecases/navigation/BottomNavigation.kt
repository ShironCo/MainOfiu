package com.example.ofiu.usecases.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ofiu.usecases.users.clientUser.chat.ChatApp
import com.example.ofiu.usecases.users.clientUser.home.HomeApp
import com.example.ofiu.usecases.users.clientUser.profile.ProfileApp

@Composable
fun BottomNavGraph(modifier: Modifier, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {

        composable(BottomBarScreen.Home.route){
            HomeApp()
        }
        composable(BottomBarScreen.Chat.route){
            ChatApp()
        }
        composable(BottomBarScreen.Profile.route){
            ProfileApp()
        }
    }
}