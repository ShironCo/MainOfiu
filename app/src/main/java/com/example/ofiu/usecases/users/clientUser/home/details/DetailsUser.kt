package com.example.ofiu.usecases.users.clientUser.home.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ofiu.R
import com.example.ofiu.usecases.session.login.LoginViewModel


@Composable
fun DetailsUserApp(id: String, navController: NavHostController) {
    Scaffold(
        topBar = { DetailsTopBar(navController = navController) }
    ) {
        DetailsUserContent(Modifier.padding(it), id = id)
    }
}

@Composable
fun DetailsTopBar(navController: NavHostController) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
    ){
        IconButton(onClick = {
            navController.popBackStack()
        }, enabled = true) {
            Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24), null)
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Opciones de perfil")
            DropdownMenu(expanded = , onDismissRequest = { /*TODO*/ }) {
                DropdownMenuItem(onClick = { /*TODO*/ }) {
                    
                }
            }
        }
    }
}

@Composable
fun DetailsUserContent(
    modifier: Modifier,
    id: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onSurface)
    ) {
    }
    Text(text = id)
}

@Composable
fun DetailsProfile() {

}