package com.example.ofiu.usecases.session.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ofiu.R
import com.example.ofiu.usecases.navigation.AppScreens

@Composable
fun LoginApp(navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()) {
    Scaffold (
        topBar = { LoginTolBar(navController, viewModel)}
    ){
        paddingValues ->
        LoginContent(modifier = Modifier.padding(paddingValues), viewModel, navController)
    }
}


@Composable
fun LoginTolBar(navController: NavHostController, viewModel: LoginViewModel){
    val backEnable : Boolean by viewModel.backEnable.observeAsState(initial = true)
    TopAppBar(
        title = {
            IconButton(onClick = { viewModel.onBackEnable()
                         navController.popBackStack()
                                 }, enabled = backEnable) {
                   Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24),null, )
            }
        }, backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
    )
}

@Composable
fun LoginContent(modifier: Modifier, viewModel: LoginViewModel, navController: NavHostController){

    val email : String by viewModel.email.observeAsState(initial = "")
    val password : String by viewModel.password.observeAsState(initial = "")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onSurface)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(350.dp)
                .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                .background(MaterialTheme.colors.background)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
        ) {
            Text(stringResource(id = R.string.login),
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier.padding(top = 40.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
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
                    
                    TextFieldLoginEmail(email) {viewModel.onTextLoginChange(it, password) }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    TextFieldLoginPassword(password,{viewModel.onTextLoginChange(email, it)}, viewModel)
                    Spacer(modifier = Modifier.height(30.dp))

                    ButtonLogin(viewModel, navController)

                    Spacer(modifier = Modifier.height(2.dp))
                    TextButton(onClick = { /*TODO*/ },
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = Color.Transparent,
                        disabledContentColor = Color.Transparent,
                        contentColor = Color.Transparent
                    )) {
                        TextButton(onClick = {navController.navigate(AppScreens.ForgotPassword.route)}) {
                            Text(
                                stringResource(id = R.string.forgotPass),
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.secondaryVariant,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        stringResource(id = R.string.loginapp),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.secondaryVariant
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row() {
                        Image(
                            painter = painterResource(id = R.drawable.google___original),
                            contentDescription = "Logo de Google"
                        )
                        Spacer(modifier = Modifier.width(1.dp))
                        Image(
                            painter = painterResource(id = R.drawable.facebook___original),
                            contentDescription = "Logo de Google"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ButtonLogin(viewModel: LoginViewModel, navController: NavHostController){
    Button(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .clip(MaterialTheme.shapes.small),
        shape = MaterialTheme.shapes.small,
        onClick = {
            viewModel.onLoginSelected()
            navController.navigate(AppScreens.Menu.route)
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primaryVariant,
        ),
    ) {
        Text(stringResource(id = R.string.login),
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.secondary)
    }
}

@Composable
fun TextFieldLoginEmail(email: String, onTextLoginChange:(String) -> Unit){
    TextField(value = email,
        onValueChange = {onTextLoginChange(it)},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
        singleLine = true,
        leadingIcon = {
            Image(painter = painterResource(id = R.drawable.baseline_person_24),
                contentDescription = "Icon Person"
            )},
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        shape = MaterialTheme.shapes.medium,
        textStyle = MaterialTheme.typography.subtitle2.copy(MaterialTheme.colors.onSecondary)
    )


}
@Composable
    fun TextFieldLoginPassword(
    password: String,
    onTextLoginChange: (String) -> Unit,
    viewModel: LoginViewModel

) {
    val visibilityButton : Boolean by viewModel.visibilityButton.observeAsState(initial = false)
    val iconVisibility: Int = if (visibilityButton){
        R.drawable.baseline_visibility_24
    } else {
        R.drawable.baseline_visibility_off_24
    }
    val focusManager = LocalFocusManager.current
    TextField(value = password,
        onValueChange = { onTextLoginChange(it) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions (onDone = {focusManager.clearFocus()}),
        leadingIcon = {
            Image(painter = painterResource(id = R.drawable.baseline_lock_24),
                contentDescription = "Icon Person"
            )},
        trailingIcon = {
                       IconButton(onClick = {viewModel.onVisibilityButton()}) {
                           Icon(
                               painter = painterResource(iconVisibility),
                               null,
                               tint = Color(0xFF969696))
                       }
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,),
        shape = MaterialTheme.shapes.medium,
        textStyle = MaterialTheme.typography.subtitle2.copy(MaterialTheme.colors.onSecondary),
        visualTransformation = if (visibilityButton) VisualTransformation.None else PasswordVisualTransformation()
    )
}
