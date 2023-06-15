package com.example.ofiu.usecases.users.clientUser.profile

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ofiu.R

@Composable
fun ProfileApp(navControllerMain: NavHostController, viewModel: UserClientProfileViewModel = hiltViewModel()) {

    val expandMenu: Boolean by viewModel.expandMenu.observeAsState(initial = false)

    Scaffold(
        topBar = {
            UserProfileTopBar(navControllerMain = navControllerMain, expandMenu = expandMenu, viewModel = viewModel)
        }
    ) {
        ProfileContent(modifier = Modifier.padding(it), viewModel)
    }
}


@Composable
fun UserProfileTopBar(
    navControllerMain: NavHostController,
    expandMenu: Boolean,
    viewModel: UserClientProfileViewModel
) {
    val openLink = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ){}

    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Perfil",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier.padding(start = 30.dp)
            )
            IconButton(onClick = {
                viewModel.onExpandMenu()
            }) {
                Icon(
                    imageVector = Icons.Default.MoreVert, contentDescription = "Opciones de perfil",
                    tint = MaterialTheme.colors.onPrimary
                )
                DropdownMenu(
                    expanded = expandMenu,
                    onDismissRequest = {
                        viewModel.onExpandMenu()
                    },
                    modifier = Modifier.background(MaterialTheme.colors.onPrimary)
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
                        onClick = {
                            val url = "https://ofiu.online/Politicas/TérminosycondicionesdeUso.pdf"
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse(url)
                            }
                            openLink.launch(intent)
                        }
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
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier,
    viewModel: UserClientProfileViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onSurface)
    ) {
        ProfileData(viewModel = viewModel)
    }


}

@Composable
fun ProfileData(viewModel: UserClientProfileViewModel) {
    val context = LocalContext.current
    val imageProfile: String = "0"
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        if (it != null) {
            viewModel.setImageProfile(it)
            viewModel.updatePhotoProfile(it, context)
        }
    }
    Card(
        backgroundColor = MaterialTheme.colors.onPrimary,
        elevation = 4.dp,
        modifier = Modifier.wrapContentSize()
    ) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {Box(
                modifier = Modifier
                    .size(80.dp), contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30))
                        .size(70.dp)
                        .background(MaterialTheme.colors.background)
                ) {
                    if (imageProfile.toString() != "0") {
                        AsyncImage(model = imageProfile, contentDescription = null,
                            contentScale = ContentScale.Crop, modifier = Modifier.clickable {
                                launcher.launch("image/*")
                            })
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null, tint = MaterialTheme.colors.onSurface,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    launcher.launch("image/*")
                                })
                    }
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(20.dp)
                            .background(MaterialTheme.colors.onPrimary)
                            .padding(3.dp)
                    ) {
                        Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = null,
                            tint = MaterialTheme.colors.background)
                    }
                }
            }
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Juan Pablo",
                    style = MaterialTheme.typography.h2.copy(
                        fontSize = 20.sp,
                        shadow = Shadow(
                            MaterialTheme.colors.secondaryVariant,
                            Offset(1f, 1f),
                            blurRadius = 2f
                        )
                    )
                )
            }
            Text(
                text = stringResource(id = R.string.profileWorkerContactTitle),
                style = MaterialTheme.typography.h2.copy(
                    fontSize = 18.sp
                ),
                color = MaterialTheme.colors.secondaryVariant
            )
        }
    }
}


