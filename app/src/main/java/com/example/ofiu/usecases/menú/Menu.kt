package com.example.ofiu.usecases.menú

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ofiu.R
import com.example.ofiu.remote.dto.UserResponse
import com.example.ofiu.usecases.navigation.AppScreens

@Composable
fun MenuApp(navController: NavHostController, viewModel: MenuViewModel = hiltViewModel()) {
    MenuContent(navController, viewModel)
}

@Composable
fun MenuContent(navController: NavHostController, viewModel: MenuViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp, start = 30.dp, end = 30.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.rol), 
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )
        MenuImage(img = R.drawable.client, R.string.client){
            viewModel.onChangeUser("0", context, navController)
        }
        Spacer(modifier = Modifier.height(10.dp))
        MenuImage(img = R.drawable.worker, R.string.professional){
            navController.popBackStack()
            navController.navigate(AppScreens.DrawerScreen.route)
            viewModel.onChangeUser("1", context, navController)
        }
    }
}

@Composable
fun MenuImage(img: Int, text: Int, nav: () -> Unit) {
    Button(onClick = { nav() },
        elevation = ButtonDefaults.elevation(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent
        )
    ) {
        Column (
            modifier = Modifier.padding(10.dp)
                ){
            Icon(
                painter = painterResource(id = img),
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(text),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}