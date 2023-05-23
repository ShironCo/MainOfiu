package com.example.ofiu.usecases.session.login.forgotPassword

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ofiu.R
import com.example.ofiu.usecases.navigation.AppScreens


@Composable
fun ForgotPasswordTwo(
    navController: NavController,
    email: String?,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { ForgotPasswordTopBarTwo(navController) }
    ) { paddingValues ->
        ForgotPasswordContentTwo(Modifier.padding(paddingValues), viewModel, navController, email)
    }
}

@Composable
fun ForgotPasswordTopBarTwo(navController: NavController) {
    TopAppBar(
        title = {
            IconButton(onClick = { navController.popBackStack() }) {
                Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24), null)
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
    )
}

@Composable
fun ForgotPasswordContentTwo(
    modifier: Modifier,
    viewModel: ForgotPasswordViewModel,
    navController: NavController,
    email: String?
) {

    val button: Boolean by viewModel.buttonValidation.observeAsState(initial = false)
    val code: String by viewModel.code.observeAsState(initial = "")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onSurface)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                .background(MaterialTheme.colors.background)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.forgotPassword),
                style = MaterialTheme.typography.h1.copy(fontSize = 25.sp),
                color = MaterialTheme.colors.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(30.dp),
                elevation = 8.dp,
                shape = RoundedCornerShape(30.dp),
                color = MaterialTheme.colors.onPrimary
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 30.dp, start = 20.dp, end = 20.dp, bottom = 30.dp)
                        .wrapContentWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.recoverEmail) + " $email",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.secondaryVariant
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    EmailTextFieldTwo(viewModel, code)
                    Spacer(modifier = Modifier.height(30.dp))
                    ForgotPasswordButtonTwo(email, button, navController)
                }
            }
        }
    }
}

@Composable
fun EmailTextFieldTwo(viewModel: ForgotPasswordViewModel, code: String) {
    TextField(
        value = code,
        onValueChange = { viewModel.onTextChangeTwo(it) },
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(R.string.enterCode),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.body2
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        shape = MaterialTheme.shapes.medium,
        textStyle = MaterialTheme.typography.subtitle2.copy(MaterialTheme.colors.onSecondary),
    )
}

@Composable
fun ForgotPasswordButtonTwo(email: String?, button: Boolean, navController: NavController) {
    val context = LocalContext.current
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(MaterialTheme.shapes.small),
        shape = MaterialTheme.shapes.small,
        onClick = {
            navController.navigate(AppScreens.ForgotPasswordThree.route + "/$email")
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primaryVariant,
        ), enabled = button
    ) {
        Text(
            stringResource(id = R.string.recover),
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.secondary
        )
    }
}