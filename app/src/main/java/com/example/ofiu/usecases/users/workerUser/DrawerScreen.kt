package com.example.ofiu.usecases.users.workerUser

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.ofiu.R
import com.example.ofiu.usecases.navigation.DrawerNavGraph
import com.example.ofiu.usecases.navigation.DrawerScreens
import kotlinx.coroutines.launch


@Composable
fun DrawerScreen() {
    val navController = rememberNavController()
    val screens = listOf(
        DrawerScreens.Profile,
        DrawerScreens.Chat,
        DrawerScreens.VerifyId
    )
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var title by remember{ mutableStateOf(DrawerScreens.Profile.title) }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopDrawerBar(title = title) {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }
        },
        drawerContent = {
            DrawerHeader()
            DrawerBody(items = screens, onItemClick = {
                when (it.route) {
                    DrawerScreens.Profile.route -> {
                        title = DrawerScreens.Profile.title
                        navController.popBackStack()
                        navController.navigate(DrawerScreens.Profile.route)
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                    DrawerScreens.Chat.route -> {
                        title = DrawerScreens.Chat.title
                        navController.popBackStack()
                        navController.navigate(DrawerScreens.Chat.route)
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                    DrawerScreens.VerifyId.route -> {

                    }
                    else -> {}
                }
            })
        },
        drawerShape = MaterialTheme.shapes.small
    ) { padding ->
        DrawerNavGraph(modifier = Modifier.padding(padding), navController = navController)
    }
}

@Composable
fun TopDrawerBar(title: String, onClickDrawe: () -> Unit) {
    TopAppBar(
        title = { Text(text = title) },
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onSurface,
        navigationIcon = {
            IconButton(onClick = { onClickDrawe() }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        }
    )
}

@Composable
fun DrawerHeader() {
    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(vertical = 24.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DrawerBody(
    items: List<DrawerScreens>,
    onItemClick: (DrawerScreens) -> Unit
) {
    Box(modifier = Modifier
        .background(MaterialTheme.colors.background)
        .fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onItemClick(item)
                        }
                        .padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(item.img),
                        contentDescription = item.title,
                        tint = MaterialTheme.colors.onSurface
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        modifier = Modifier.weight(1f),
                        text = item.title,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}