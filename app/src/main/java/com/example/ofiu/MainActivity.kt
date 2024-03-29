package com.example.ofiu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.ofiu.ui.theme.OfiuTheme
import com.example.ofiu.usecases.navigation.AppNavigation
import com.example.ofiu.usecases.users.clientUser.chat.ChatApp
import com.example.ofiu.usecases.users.clientUser.chat.messages.MessageApp
import com.example.ofiu.usecases.users.clientUser.home.HomeApp
import com.example.ofiu.usecases.users.clientUser.home.details.DetailsUserApp
import com.example.ofiu.usecases.users.clientUser.home.details.DialogComment
import com.example.ofiu.usecases.users.clientUser.profile.ProfileApp
import com.example.ofiu.usecases.users.workerUser.profile.ProfileWorkerApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                OfiuTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                   AppNavigation()
                    //ProfileApp()
                    //HomeApp()
                 // DetailsUserApp("ha")
                   // DialogComment(ratint = 0, onRatingChanged = {}, value = "31" , onValueChange = {}) {
                    //ChatApp()
                  //  MessageApp()

                    }
                }
            }
        }
    }

