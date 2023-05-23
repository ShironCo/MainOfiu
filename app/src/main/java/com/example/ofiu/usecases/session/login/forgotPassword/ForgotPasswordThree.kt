package com.example.ofiu.usecases.session.login.forgotPassword

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ofiu.R
import com.example.ofiu.usecases.navigation.AppScreens

@Composable
fun ForgotPasswordThree(
    navController: NavController,
    email: String?,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { ForgotPasswordTopBarThree(navController) }
    ) { paddingValues ->
        ForgotPasswordContentThree(Modifier.padding(paddingValues), viewModel, navController, email)
    }
}

@Composable
fun ForgotPasswordTopBarThree(navController: NavController) {
    TopAppBar(
        title = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24), null)
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ForgotPasswordContentThree(
    modifier: Modifier,
    viewModel: ForgotPasswordViewModel,
    navController: NavController,
    email: String?
) {

    val button: Boolean by viewModel.buttonValidation.observeAsState(initial = false)
    val password: String by viewModel.password.observeAsState(initial = "")
    val passwordRepeat: String by viewModel.passwordRepeat.observeAsState(initial = "")
    val changeStep: Boolean by viewModel.changeStep.observeAsState(initial = false)
    val visibilityButton: Boolean by viewModel.visibilityButton.observeAsState(initial = false)

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
                    if (!changeStep) {
                        StepThree(
                            viewModel,
                            password,
                            passwordRepeat,
                            button,
                            email,
                            visibilityButton
                        )
                    }
                    AnimatedVisibility(
                        visible = changeStep,
                        enter = scaleIn()
                    ) {
                        StepFourth(email, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun StepFourth(email: String?, navController: NavController) {
    Column {
        Text(
            text = stringResource(id = R.string.updateSuccessful) + " $email",
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.secondaryVariant
        )
        Spacer(modifier = Modifier.height(30.dp))
        ForgotPasswordButtonThree(R.string.login, true) {
            navController.navigate(AppScreens.Login.route)
        }
    }

}

@Composable
fun StepThree(
    viewModel: ForgotPasswordViewModel,
    password: String,
    passwordRepeat: String,
    button: Boolean,
    email: String?,
    visibilityButton: Boolean
) {
    Column() {
        Text(
            text = stringResource(id = R.string.updatePassword) + " $email",
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.secondaryVariant
        )
    }
    Spacer(modifier = Modifier.height(30.dp))
    PasswordTextFieldThree(R.string.newPassword, password, visibilityButton, {viewModel.onTextChangeThree(it, passwordRepeat)},
        {viewModel.onVisibilityButton()})
    Spacer(modifier = Modifier.height(30.dp))
    PasswordTextFieldThree(R.string.newPasswordRepeat, passwordRepeat, visibilityButton, {viewModel.onTextChangeThree(password, it)}) {
        viewModel.onVisibilityButton()
    }
    Spacer(modifier = Modifier.height(30.dp))
    ForgotPasswordButtonThree(R.string.change, button) {
        viewModel.onStepChange()
    }
}

@Composable
fun PasswordTextFieldThree(
    text: Int,
    password: String,
    visibilityButton: Boolean,
    onTextLoginChange: (String) -> Unit,
    onVisibilityButton: () -> Unit
) {
    TextField(
        value = password,
        onValueChange = { onTextLoginChange(it) },
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(text),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.body2
            )
        },
        trailingIcon = {
            IconButton(onClick = {onVisibilityButton()}) {
                Icon(
                    painter = if (visibilityButton) painterResource(R.drawable.baseline_visibility_24) else painterResource(R.drawable.baseline_visibility_off_24),
                    contentDescription = null,
                    tint = Color(0xFF969696)
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        shape = MaterialTheme.shapes.medium,
        textStyle = MaterialTheme.typography.subtitle2.copy(MaterialTheme.colors.onSecondary),
        visualTransformation = if (visibilityButton) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@Composable
fun ForgotPasswordButtonThree(text: Int, button: Boolean, onFunction: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(MaterialTheme.shapes.small),
        shape = MaterialTheme.shapes.small,
        onClick = {
            onFunction()
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primaryVariant,
        ), enabled = button
    ) {
        Text(
            stringResource(text),
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.secondary
        )
    }
}