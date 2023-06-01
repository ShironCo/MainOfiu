package com.example.ofiu.usecases.users.workerUser.verifyId

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ofiu.R
import com.example.ofiu.usecases.navigation.AppScreens
import com.example.ofiu.usecases.session.login.LoginViewModel

@Composable
fun VerifyWorkerApp(navHostController: NavHostController, viewModel: VerifyViewModel = hiltViewModel()) {
    Scaffold(
        topBar = { VerifyTopBar(navHostController) }
    ) { paddingValues ->
        VerifyContent(modifier = Modifier.padding(paddingValues), navHostController, viewModel)
    }
}

@Composable
fun VerifyTopBar(navController: NavHostController) {
    //  val backEnable: Boolean by viewModel.backEnable.observeAsState(initial = true)
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
                    text = stringResource(id = R.string.verifyAccount),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onSurface
                )
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 4.dp,
    )
}

@Composable
fun VerifyContent(modifier: Modifier, navController: NavHostController, viewModel: VerifyViewModel) {

    val image1 : String = viewModel.getDataPreference("imageFrontal")
    val image2 : String = viewModel.getDataPreference("imageTrasera")
    var validButtonId: Boolean = true
    if (image1.isNotBlank() && image2.isNotBlank()){
        validButtonId = false
    }

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
        ){
            navController.navigate(AppScreens.VerifyId.route)
        }
        Spacer(modifier = Modifier.height(30.dp))
        VerifyCard(
            title = R.string.verifyFace,
            image = R.drawable.verifyaccount,
            sizeH = 50.dp,
            sizeW = 50.dp,
            true
        ){
            navController.navigate(AppScreens.VerifyFace.route)
        }
    }

}

@Composable
fun VerifyCard(title: Int, image: Int, sizeH: Dp, sizeW: Dp, validButton: Boolean, navigate: () -> Unit) {
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
                        if(!validButton) {ColorFilter.colorMatrix(colorMatrix = ColorMatrix().apply { setToSaturation(0f) })}
                        else { ColorFilter.colorMatrix(colorMatrix = ColorMatrix().apply { setToSaturation(1f) })}
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
                        tint = if (validButton) {MaterialTheme.colors.background} else {Color.LightGray},
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )
                }
        }
    }
}

