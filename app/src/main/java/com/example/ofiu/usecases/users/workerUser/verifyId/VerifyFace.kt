package com.example.ofiu.usecases.users.workerUser.verifyId

import android.Manifest
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.Toast
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ofiu.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@Composable
fun VerifyFaceApp(navController: NavHostController, viewModel: VerifyViewModel = hiltViewModel()) {
    Scaffold(
        topBar = { VerifyFaceTopBar(navController) }
    ) { paddingValues ->
        VerifyFaceContent(modifier = Modifier.padding(paddingValues), viewModel, navController)
    }
}

@Composable
fun VerifyFaceTopBar(navController: NavHostController) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = {
                    navController.popBackStack()
                }, enabled = true) {
                    Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24), null)
                }
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = stringResource(id = R.string.verifyId),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onSurface
                )
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 4.dp,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun VerifyFaceContent(modifier: Modifier, viewModel: VerifyViewModel, navController: NavHostController) {

    val changeView: Boolean by viewModel.changeView.observeAsState(initial = false)
    val previewImage: ByteArray by viewModel.previewView.observeAsState(initial = ByteArray(0))

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWith = configuration.screenWidthDp.dp

    if (!changeView) {
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.onSurface)
                .padding(top = 40.dp, start = 30.dp, end = 30.dp, bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.face),
                contentDescription = null, Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.verifyFaceDesc),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(50.dp))
            VerifyFaceItem(text = R.string.verifyIdItem1)
            Spacer(modifier = Modifier.height(10.dp))
            VerifyFaceItem(text = R.string.verifyIdItem2)
            Spacer(modifier = Modifier.height(10.dp))
            VerifyFaceItem(text = R.string.verifyIdItem3)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(Alignment.Bottom)
            ) {
                VerifyFaceButton(title = R.string.verifyFaceButton) {
                    viewModel.onTextChange(NameImages.ImageFace.image)
                    viewModel.onChangeView()
                }
            }
        }
    }
    AnimatedVisibility(
        visible = changeView, enter = scaleIn(), modifier = Modifier
            .fillMaxSize()
    ) {
        TakeImageFace(viewModel = viewModel, screenWith, screenHeight)
    }
    if (previewImage.isNotEmpty()){
        val bitmapImage = BitmapFactory.decodeByteArray(previewImage, 0, previewImage.size, null)
        val painter = BitmapPainter(bitmapImage.asImageBitmap())
        PreviewImageFace(painter = painter, screenHeight = screenHeight , screenWidth = screenWith, viewModel, navController)
    }
}

@Composable
fun VerifyFaceItem(text: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        CheckFace()
        Spacer(modifier = Modifier.width(10.dp))
        VerifyFaceText(text)
    }
}

@Composable
fun CheckFace() {
    Icon(
        painter = painterResource(id = R.drawable.baseline_check_24),
        contentDescription = null, tint = MaterialTheme.colors.background,
        modifier = Modifier.size(20.dp)
    )
}

@Composable
fun VerifyFaceText(text: Int) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(text),
        style = MaterialTheme.typography.subtitle1,
        color = MaterialTheme.colors.onSecondary,
        textAlign = TextAlign.Start
    )
}

@Composable
fun VerifyFaceButton(title: Int, verify: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = MaterialTheme.shapes.small,
        onClick = {
            verify()
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primaryVariant,
        ),
        enabled = true
    ) {
        Text(
            stringResource(title),
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.secondary
        )
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TakeImageFace(viewModel: VerifyViewModel, screenWith: Dp, screenHeight: Dp) {

    val permission = if (Build.VERSION.SDK_INT <= 28) {
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    } else listOf(
        Manifest.permission.CAMERA
    )
    val permissionState = rememberMultiplePermissionsState(permissions = permission)
    if (!permissionState.allPermissionsGranted) {
        SideEffect {
            permissionState.launchMultiplePermissionRequest()
        }
    }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var previewView: PreviewView
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (permissionState.allPermissionsGranted) {
            Box(
                modifier = Modifier
                    .height(screenHeight * 0.85f)
                    .width(screenWith)
            ) {
                AndroidView(
                    factory = {
                        previewView = PreviewView(it)
                        viewModel.showCameraPreview(previewView, lifecycleOwner, false)
                        previewView
                    },
                    modifier = Modifier
                        .height(screenHeight * 0.85f)
                        .width(screenWith)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                ) {
                    Column(modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.id) ,
                            contentDescription = null, Modifier.size(screenWith * 0.8f, screenHeight * 0.8f),
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .height(screenHeight * 0.15f)
            ,
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = {
                if (permissionState.allPermissionsGranted) {
                    viewModel.capture(context)
                } else {
                    Toast.makeText(
                        context,
                        "Porfavor acepta los permisos en la configuracion de la app",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_motion_photos_on_24), contentDescription = null,
                    modifier = Modifier.size(45.dp), tint = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}


@Composable
fun PreviewImageFace(painter: Painter, screenHeight : Dp, screenWidth : Dp, viewModel: VerifyViewModel, navController: NavHostController){
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)) {
        Image(painter = painter ,
            contentDescription = null,
            Modifier.size(screenWidth, screenHeight * 0.85f)
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                viewModel.onCleanPreviewImage()
            }, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .height(screenHeight * 0.15f)
                .padding(start = 10.dp, end = 5.dp, bottom = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onSurface)) {
                Text(
                    text = "Volver a tomar foto",
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.background
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = {
                viewModel.onSaveImage(context, navController)
                navController.popBackStack()
            }, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .height(screenHeight * 0.15f)
                .padding(start = 5.dp, end = 10.dp, bottom = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onSurface)) {
                Text(
                    text = "Continuar",
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.background
                )
            }
        }
    }
}