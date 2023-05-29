package com.example.ofiu.usecases.session.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.platform.LocalFocusManager
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
import com.example.ofiu.remote.dto.UserResponse


@Composable
fun RegisterApp(navController: NavController, viewModel: RegisterViewModel = hiltViewModel()) {
    Scaffold(
        topBar = { RegisterTopBar(viewModel, navController) }
    ) { paddingValues ->
        RegisterContent(Modifier.padding(paddingValues), viewModel)
    }
}

@Composable
fun RegisterTopBar(viewModel: RegisterViewModel, navController: NavController) {
    val backEnable: Boolean by viewModel.backEnable.observeAsState(initial = true)
    TopAppBar(
        title = {
            IconButton(onClick = {
                viewModel.onBackEnable()
                navController.popBackStack()
            }, enabled = backEnable) {
                Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24), null)
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
    )
}

@Composable
fun RegisterContent(modifier: Modifier, viewModel: RegisterViewModel) {

    val name: String by viewModel.name.observeAsState(initial = "")
    val lastName: String by viewModel.lastName.observeAsState(initial = "")
    val email: String by viewModel.email.observeAsState(initial = "")
    val phone: String by viewModel.phone.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val passwordRepeat: String by viewModel.passwordRepeat.observeAsState(initial = "")
    val passwordEnable: Boolean by viewModel.passwordEnable.observeAsState(initial = false)
    val passwordVal: Boolean by viewModel.passwordVal.observeAsState(initial = false)
    val visibility: Boolean by viewModel.visibility.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val validButton: Boolean by viewModel.validButton.observeAsState(initial = false)
    val messageIndicator: UserResponse by viewModel.response.observeAsState(
        UserResponse("")
    )

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
                .padding(top = 0.dp, start = 30.dp, end = 30.dp, bottom = 30.dp)
        ) {
            Text(
                stringResource(id = R.string.register),
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.secondary,
            )
            Spacer(modifier = Modifier.height(20.dp))
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
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
                        .wrapContentWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RegisterTextFieldName(name) {
                        viewModel.onTextChange(
                            it,
                            lastName,
                            email,
                            phone,
                            password,
                            passwordRepeat
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    RegisterTextFieldLastName(lastName) {
                        viewModel.onTextChange(
                            name,
                            it,
                            email,
                            phone,
                            password,
                            passwordRepeat
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    RegisterTextFieldEmail(email) {
                        viewModel.onTextChange(
                            name,
                            lastName,
                            it,
                            phone,
                            password,
                            passwordRepeat
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    RegisterTextFieldPhone(phone) {
                        viewModel.onTextChange(
                            name,
                            lastName,
                            email,
                            it,
                            password,
                            passwordRepeat
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    RegisterTextFieldPassword(password, visibility, viewModel) {
                        viewModel.onTextChange(
                            name,
                            lastName,
                            email,
                            phone,
                            it,
                            passwordRepeat
                        )
                    }
                    TextsEnable(textAlign = TextAlign.Start, passwordEnable, R.string.valPass)
                    Spacer(modifier = Modifier.height(10.dp))
                    RegisterTextFieldPasswordRepeat(passwordRepeat, visibility, viewModel) {
                        viewModel.onTextChange(
                            name,
                            lastName,
                            email,
                            phone,
                            password,
                            it
                        )
                    }
                    TextsEnable(textAlign = TextAlign.Start, passwordVal, R.string.passVal)
                    Spacer(modifier = Modifier.height(30.dp))
                    ButtonRegister(
                        viewModel,
                        isLoading,
                        validButton,
                        name,
                        lastName,
                        email,
                        phone,
                        password
                    )
                    Text(text = messageIndicator.response)
                }
            }
        }
    }
}

@Composable
fun RegisterTextFieldName(name: String, onTextChange: (String) -> Unit) {
    TextField(
        value = name,
        onValueChange = { onTextChange(it) },
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(R.string.name),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.body2
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
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
fun RegisterTextFieldLastName(lastName: String, onTextChange: (String) -> Unit) {
    TextField(
        value = lastName,
        onValueChange = { onTextChange(it) },
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(R.string.lastName),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.body2
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
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
fun RegisterTextFieldEmail(email: String, onTextChange: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = { onTextChange(it) },
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(R.string.email),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.body2
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
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
fun RegisterTextFieldPhone(phone: String, onTextChange: (String) -> Unit) {
    TextField(
        value = phone,
        onValueChange = { onTextChange(it) },
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(R.string.phone),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.body2
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
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
fun RegisterTextFieldPassword(
    password: String,
    visibility: Boolean,
    viewModel: RegisterViewModel,
    onTextChange: (String) -> Unit
) {
    TextField(
        value = password,
        onValueChange = { onTextChange(it) },
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(R.string.password),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.body2
            )
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
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.baseline_lock_24),
                contentDescription = "Icon Person"
            )
        },
        trailingIcon = {
            IconButton(onClick = { viewModel.onVisibility() }) {
                Icon(
                    painter = painterResource(if (visibility) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24),
                    null,
                    tint = Color(0xFF969696)
                )
            }
        },
        shape = MaterialTheme.shapes.medium,
        textStyle = MaterialTheme.typography.subtitle2.copy(MaterialTheme.colors.onSecondary),
        visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@Composable
fun RegisterTextFieldPasswordRepeat(
    passwordRepeat: String,
    visibility: Boolean,
    viewModel: RegisterViewModel,
    onTextChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = passwordRepeat,
        onValueChange = { onTextChange(it) },
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(R.string.passwordRepeat),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.body2
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.baseline_lock_24),
                contentDescription = "Icon Person"
            )
        },
        trailingIcon = {
            IconButton(onClick = { viewModel.onVisibility() }) {
                Icon(
                    painter = painterResource(if (visibility) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24),
                    null,
                    tint = Color(0xFF969696)
                )
            }
        },
        shape = MaterialTheme.shapes.medium,
        textStyle = MaterialTheme.typography.subtitle2.copy(MaterialTheme.colors.onSecondary),
        visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@Composable
fun ButtonRegister(
    viewModel: RegisterViewModel,
    isLoading: Boolean,
    validButton: Boolean,
    name: String,
    lastName: String,
    email: String,
    phone: String,
    password: String
) {
    val context = LocalContext.current
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(MaterialTheme.shapes.small),
        shape = MaterialTheme.shapes.small,
        onClick = {
            Toast.makeText(context, "$name $lastName $email $phone $password", Toast.LENGTH_LONG).show()
            viewModel.onButtonClick(name, lastName, email, phone, password)
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primaryVariant,
        ), enabled = validButton
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            Text(
                stringResource(id = R.string.register),
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.secondary
            )
        }
    }
}

@Composable
fun TextsEnable(textAlign: TextAlign, modienable: Boolean, emailVal: Int) {
    if (!modienable) {
        Text(
            text = stringResource(emailVal),
            color = MaterialTheme.colors.background,
            style = MaterialTheme.typography.body2.copy(fontSize = 14.sp),
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp)
                .fillMaxWidth(),
            textAlign = textAlign,
        )
    }
}



