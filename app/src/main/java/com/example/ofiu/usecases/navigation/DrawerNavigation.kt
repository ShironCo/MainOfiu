package com.example.ofiu.usecases.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ofiu.usecases.users.workerUser.DrawerScreenViewModel
import com.example.ofiu.usecases.users.workerUser.chat.ChatWorkerApp

import com.example.ofiu.usecases.users.workerUser.profile.ProfileWorkerApp
import com.example.ofiu.usecases.users.workerUser.verifyId.VerifyWorkerApp

@Composable
fun DrawerNavGraph(modifier: Modifier,
                   navController: NavHostController) {


    NavHost(
        navController = navController,
        startDestination = DrawerScreens.Profile.route

    ) {

        composable(DrawerScreens.Profile.route){
            ProfileWorkerApp()
        }
        composable(DrawerScreens.Chat.route){
            ChatWorkerApp()
        }
    }
}