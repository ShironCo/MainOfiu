package com.example.ofiu.usecases.users.workerUser.verifyId

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ofiu.R
import com.example.ofiu.usecases.navigation.AppScreens

@Composable
fun VerifyWorkerApp(
    navHostController: NavHostController,
    viewModel: VerifyViewModel = hiltViewModel()
) {
    val alertDialog: Int by viewModel.showAlertDialog.observeAsState(initial = 0)
    VerifyContent(navHostController, viewModel)
    BackHandler(true) {
        viewModel.onTextChange(null, 2)
    }
    if (viewModel.verifySuccessful()){
        DialogVerify(title = R.string.verifySuccessful){
            navHostController.popBackStack()
        }
    }
    if (alertDialog == 1) {
        Dialog(onDismissRequest = {}
        ) {
            Card(
                shape = MaterialTheme.shapes.medium,
                backgroundColor = MaterialTheme.colors.onSurface,
            )
            {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.cheque),
                        contentDescription = null,
                        Modifier
                            .size(140.dp)
                            .padding(20.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.verifyProcess),
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.secondaryVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            top = 0.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 20.dp
                        )
                    )
                }
            }
        }
    }
    if (alertDialog == 2) {
        Dialog(
            onDismissRequest = { viewModel.onTextChange(null, 0)} ) {
            Card(
                shape = MaterialTheme.shapes.medium,
                backgroundColor = MaterialTheme.colors.onSurface,
            )
            {
                Column(
                    modifier = Modifier.padding(30.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.verifyEndTitle),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.background,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(
                            onClick = {
                                viewModel.exit(navHostController)
                            }, colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.onError
                            )
                        ) {
                            Text(
                                text = "Cancelar",
                                style = MaterialTheme.typography.subtitle1,
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                viewModel.onTextChange(null, 0)
                            }, colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.background
                            )
                        ) {
                            Text(
                                text = "Verificar",
                                style = MaterialTheme.typography.subtitle1,
                                color = MaterialTheme.colors.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DialogVerify(title: Int, dismiss: ()-> Unit){
    Dialog(onDismissRequest = {
        dismiss()
    }
    ) {
        Card(
            shape = MaterialTheme.shapes.small,
            backgroundColor = MaterialTheme.colors.background,
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(title),
                    style = MaterialTheme.typography.h1,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(20.dp)
                )
            }
        }
    }
}

@Composable
fun VerifyContent(
    navController: NavHostController,
    viewModel: VerifyViewModel
) {
    val validButtonId : Boolean by viewModel.validButtonId.observeAsState(initial = true)
    val validButtonFace : Boolean by viewModel.validButtonFace.observeAsState(initial = false)
    viewModel.onValidButtonFace()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onSurface)
            .padding(start = 20.dp, top = 40.dp, end = 20.dp)
    ) {
        Text(
            text = stringResource(id = R.string.verifyDescription),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.secondaryVariant,
            textAlign = TextAlign.Justify
        )
        Spacer(modifier = Modifier.height(60.dp))
        VerifyCard(
            title = R.string.verifyId,
            image = R.drawable.verifyid,
            sizeH = 50.dp,
            sizeW = 70.dp,
            validButtonId,
        ) {
            navController.navigate(AppScreens.VerifyId.route)
        }
        Spacer(modifier = Modifier.height(30.dp))
        VerifyCard(
            title = R.string.verifyFace,
            image = R.drawable.verifyaccount,
            sizeH = 50.dp,
            sizeW = 50.dp,
            validButtonFace
        ) {
            navController.navigate(AppScreens.VerifyFace.route)
        }
    }
    if (!validButtonFace && !validButtonId) {
        SendImage(viewModel, navController)
    }
}

@Composable
fun VerifyCard(
    title: Int,
    image: Int,
    sizeH: Dp,
    sizeW: Dp,
    validButton: Boolean,
    navigate: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(enabled = validButton) {
                navigate()
            },
        elevation = 9.dp,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colors.onSurface,
    ) {
        Column(
            modifier = Modifier.padding(20.dp, 10.dp, 20.dp, 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(image),
                    null, Modifier.size(sizeW, sizeH),
                    colorFilter =
                    if (!validButton) {
                        ColorFilter.colorMatrix(colorMatrix = ColorMatrix().apply {
                            setToSaturation(
                                0f
                            )
                        })
                    } else {
                        ColorFilter.colorMatrix(colorMatrix = ColorMatrix().apply {
                            setToSaturation(
                                1f
                            )
                        })
                    }
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = stringResource(title),
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onSecondary,
                )
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_24),
                    contentDescription = null,
                    tint = if (validButton) {
                        MaterialTheme.colors.background
                    } else {
                        Color.LightGray
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End)
                )
            }
        }
    }
}


@Composable
fun SendImage(viewModel: VerifyViewModel, navHostController: NavHostController) {
    val context = LocalContext.current
    Box(
        Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.BottomCenter)
            .padding(20.dp)
    ) {
        Button(
            modifier = Modifier
                .height(50.dp),
            shape = MaterialTheme.shapes.small,
            onClick = {
                viewModel.onSendImages(navHostController, context)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.background,
            ),
            enabled = true
        ) {
            Text(
                text = "Verificar Cuenta",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}

