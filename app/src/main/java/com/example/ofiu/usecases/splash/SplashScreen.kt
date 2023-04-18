package com.example.ofiu.usecases.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ofiu.R
import com.example.ofiu.usecases.navigation.AppScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController){
    LaunchedEffect(key1 = true){
        delay(5000)
        navController.popBackStack()
        navController.navigate(AppScreens.Session.route)
    }
    Splash(Modifier.background(MaterialTheme.colors.background))
}

@Composable
fun Splash(modifier: Modifier = Modifier){
    Row(
        modifier
            .fillMaxSize()
            .padding(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(id = R.drawable.splashlogo),
            contentDescription = "Icon Ofiu",
            Modifier.weight(1F))
        Image(painter = painterResource(id = R.drawable.nombresplashlogo),
            contentDescription = "Name Ofiu",
            Modifier.weight(1F))
    }
}