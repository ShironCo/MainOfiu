package com.example.ofiu.usecases.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ofiu.R

@Composable
fun SplashScreen(navController: NavHostController, viewModel: SplashViewModel = hiltViewModel()){


    val context = LocalContext.current
    val starAnimation : Boolean by viewModel.startAnimation.observeAsState(initial = false)
    val alphaAni = animateFloatAsState(
        targetValue = if(starAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000
        )
    )


    LaunchedEffect(key1 = true){
        viewModel.setStartAnimation(true)
        viewModel.setNav(navController, context)
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
            Modifier
                .weight(1F)
                .alpha(alpha = alpha))
        Image(painter = painterResource(id = R.drawable.nombresplashlogo),
            contentDescription = "Name Ofiu",
            Modifier
                .weight(1F)
                .alpha(alpha = alpha))
    }
}

@Composable
fun dialogInternet(){
    Dialog(onDismissRequest = {
    }
    ) {
        Column() {
            Image(painter = painterResource(id = R.drawable.nowifi),
                contentDescription = "No hay internet")
            Text(text = "Revisa tu conexión a internet")
        }
    }
}