package com.example.ofiu.usecases.users.workerUser.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProfileWorkerApp(){
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.onSurface)) {
        Text(text = "Profile")
    }
}