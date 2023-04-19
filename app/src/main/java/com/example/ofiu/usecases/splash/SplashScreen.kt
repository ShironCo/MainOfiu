package com.example.ofiu.usecases.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ofiu.R
import com.example.ofiu.usecases.navigation.AppScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController){

    var starAnimation by remember {
        mutableStateOf(false)
    }
    val alphaAni = animateFloatAsState(
        targetValue = if(starAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 2000
        )
    )

    LaunchedEffect(key1 = true){
        starAnimation = true
        delay(5000)
        navController.popBackStack()
        navController.navigate(AppScreens.Session.route)
    }
    Splash(Modifier.background(MaterialTheme.colors.background), alpha = alphaAni.value)
}

@Composable
fun Splash(modifier: Modifier = Modifier, alpha: Float){
    Row(
        modifier
            .fillMaxSize()
            .padding(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(id = R.drawable.splashlogo),
            contentDescription = "Icon Ofiu",
            Modifier.weight(1F).alpha(alpha = alpha))
        Image(painter = painterResource(id = R.drawable.nombresplashlogo),
            contentDescription = "Name Ofiu",
            Modifier.weight(1F).alpha(alpha = alpha))
    }
}