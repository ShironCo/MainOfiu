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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ofiu.R

@Composable
fun ProfileApp(
    navControllerMain: NavHostController,
    viewModel: UserClientProfileViewModel = hiltViewModel()
) {
    val expandMenu: Boolean by viewModel.expandMenu.observeAsState(initial = false)
    val openDialog: Boolean by viewModel.openDialog.observeAsState(initial = false)
    Scaffold(
        topBar = {
            UserProfileTopBar(
                navControllerMain = navControllerMain,
                expandMenu = expandMenu,
                viewModel = viewModel,
            ) {
                viewModel.onOpenDialog()
            }
        }
    ) {
        ProfileContent(modifier = Modifier.padding(it), viewModel)
    }

    if (openDialog) {
        AlertDialogProfile(onCloseSession = { viewModel.onCloseSession(navController = navControllerMain) }) {
            viewModel.onOpenDialog()
        }
    }


}


@Composable
fun UserProfileTopBar(
    navControllerMain: NavHostController,
    expandMenu: Boolean,
    viewModel: UserClientProfileViewModel,
    onOpenDialog: () -> Unit
) {
    val openLink = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {}

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
                        onClick = {
                            viewModel.onChangeAccount(navControllerMain)
                        }
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
                        onClick = { onOpenDialog() },
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
            .padding(40.dp)
    ) {
        Column() {
            ProfileData(viewModel)
            ProfileInformation(viewModel)
        }
    }


}

@Composable
fun ProfileData(viewModel: UserClientProfileViewModel) {
    val name: String by viewModel.nameText.observeAsState(initial = "")
    val context = LocalContext.current
    val imageProfile: Uri by viewModel.imageProfile.observeAsState(initial = Uri.EMPTY)
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        if (it != null) {
            viewModel.setImageProfile(it)
            viewModel.updatePhotoProfile(it, context)
        }
    }
    Row(
        modifier = Modifier.wrapContentSize(Alignment.Center),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
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
                    Icon(
                        imageVector = Icons.Default.PhotoCamera, contentDescription = null,
                        tint = MaterialTheme.colors.background
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = name,
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
}


@Composable
fun ProfileInformation(
    viewModel: UserClientProfileViewModel
) {

    val email: String by viewModel.emailText.observeAsState(initial = "")
    val phone: String by viewModel.phoneText.observeAsState(initial = "")


    Column(
        modifier = Modifier
            .padding(top = 20.dp, bottom = 80.dp)
            .fillMaxSize(),
        Arrangement.SpaceBetween
    ) {
        Column() {
            Text(
                modifier = Modifier.padding(bottom = 10.dp),
                text = "Informacion de tú perfil",
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.primaryVariant
            )
            ProfileTextField("Email", email)

            Spacer(modifier = Modifier.height(20.dp))

            ProfileTextField("Número de celular", phone)
            Spacer(modifier = Modifier.height(60.dp))
        }
        Image(
            painter = painterResource(id = R.drawable.cat),
            contentDescription = null
        )
    }
}

@Composable
fun ProfileTextField(
    title: String,
    text: String,
) {
    Column() {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.secondaryVariant
        )
        Row() {
            Text(
                text = text,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Composable
fun AlertDialogProfile(
    onCloseSession: () -> Unit,
    onOpenDialog: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onOpenDialog()
        },
        title = {
            Text(text = "¿Estás seguro de que deseas cerrar sesión?")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onCloseSession()
                }
            ) {
                Text("Cerrar sesion")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onOpenDialog()
                }
            ) {
                Text("Cancelar")
            }
        },
        backgroundColor = MaterialTheme.colors.background
    )
}

