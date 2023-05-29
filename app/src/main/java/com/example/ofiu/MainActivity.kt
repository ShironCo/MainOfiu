package com.example.ofiu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.ofiu.ui.theme.OfiuTheme
import com.example.ofiu.usecases.menú.MenuApp
import com.example.ofiu.usecases.navigation.AppNavigation
import com.example.ofiu.usecases.session.login.LoginViewModel
import com.example.ofiu.usecases.session.login.forgotPassword.ForgotPassword
import com.example.ofiu.usecases.session.login.forgotPassword.ForgotPasswordTwo
import com.example.ofiu.usecases.session.register.RegisterViewModel
import com.example.ofiu.usecases.users.workerUser.verifyId.VerifyWorkerApp
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
                }
            }
        }
    }
}
