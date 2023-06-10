package com.example.ofiu.usecases.users.workerUser

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ofiu.R
import com.example.ofiu.ui.theme.Shapes
import com.example.ofiu.usecases.navigation.AppScreens
import com.example.ofiu.usecases.navigation.DrawerNavGraph
import com.example.ofiu.usecases.navigation.DrawerScreens
import kotlinx.coroutines.launch


@Composable
fun DrawerScreen(navControllerMain: NavController, viewModel: DrawerScreenViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val screens = listOf(
        DrawerScreens.Profile,
        DrawerScreens.Chat,
        DrawerScreens.VerifyId
    )
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var title by remember{ mutableStateOf(DrawerScreens.Profile.title) }
    val backHandler : Boolean by viewModel.backHandler.observeAsState(initial = false)
    val moreVertToggle: Boolean by viewModel.moreVertToggle.observeAsState(initial = true)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopDrawerBar(navControllerMain = navControllerMain, viewModel = viewModel, title = title, moreVertToggle = moreVertToggle) {
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
                        viewModel.setMoreVertToogle(true)
                        title = DrawerScreens.Profile.title
                        navController.popBackStack()
                        navController.navigate(DrawerScreens.Profile.route)
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                    DrawerScreens.Chat.route -> {
                        viewModel.setMoreVertToogle(false)
                        title = DrawerScreens.Chat.title
                        navController.popBackStack()
                        navController.navigate(DrawerScreens.Chat.route)
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                    DrawerScreens.VerifyId.route -> {
                        title = DrawerScreens.VerifyId.title
                        navController.popBackStack()
                        navControllerMain.navigate(DrawerScreens.VerifyId.route)
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                    else -> {}
                }
            })
        },
        drawerShape = RoundedCornerShape(0.dp)
    ) { padding ->
        DrawerNavGraph(modifier = Modifier.padding(padding), navController = navController)
    }
    BackHandler(
        true
    ) {
        viewModel.onBackHandler(true)
    }
    if (backHandler) {
        Dialog(
            onDismissRequest = { viewModel.onBackHandler(false)}) {
            Card(
                shape = MaterialTheme.shapes.medium,
                backgroundColor = MaterialTheme.colors.onSurface,
            )
            {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.closeSession),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.background,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                    viewModel.onBackHandler(false)
                            }, colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.onError
                            )) {
                            Text(
                                text = "Cancelar",
                                style = MaterialTheme.typography.subtitle1.copy(
                                    fontSize = 14.sp
                                ),
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                viewModel.onCloseSession(navControllerMain)
                            }, colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.background
                            )
                        ) {
                            Text(
                                text = "Cerrar sesión",
                                style = MaterialTheme.typography.subtitle1.copy(
                                    fontSize = 14.sp
                                ),
                                color = MaterialTheme.colors.onSurface,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopDrawerBar(navControllerMain: NavController, viewModel: DrawerScreenViewModel, title: String, moreVertToggle: Boolean,onClickDrawer: () -> Unit) {
    
    val showVertMore: Boolean by viewModel.showVertMore.observeAsState(initial = false)

    TopAppBar(
        title = { Text(text = title) },
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onSurface,
        navigationIcon = {
            IconButton(onClick = { onClickDrawer() }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        actions = {
            if (moreVertToggle) {
                IconButton(onClick = {viewModel.setShowVertMore(true)}) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More vert"
                    )
                }
                DropdownMenu(
                    expanded = showVertMore,
                    onDismissRequest = {viewModel.setShowVertMore(false)},
                    modifier = Modifier
                        .background(MaterialTheme.colors.onSurface)
                ) {
                    DropdownMenuItem(
                        onClick = { viewModel.onChangeAccount(navControllerMain) }
                    ) {
                        Text(
                            text = stringResource(id = R.string.MoreVertItemCam),
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.secondaryVariant
                        )
                    }
                    DropdownMenuItem(
                        onClick = { /*TODO*/ }
                    ) {
                        Text(
                            text = stringResource(id = R.string.MoreVertItemTerm),
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.secondaryVariant
                        )
                    }
                    DropdownMenuItem(
                        onClick = {viewModel.onCloseSession(navController = navControllerMain)},
                    ) {
                        Text(
                            text = stringResource(id = R.string.MoreVertItemClose),
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.onError
                        )
                    }
                }
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