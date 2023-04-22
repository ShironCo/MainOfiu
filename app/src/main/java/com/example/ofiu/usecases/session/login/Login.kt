package com.example.ofiu.usecases.session.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ofiu.R
import com.example.ofiu.usecases.navigation.AppScreens

@Composable
fun LoginApp() {
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
                    TextField(value = "", 
                        onValueChange = {}, 
                        singleLine = true,
                        leadingIcon = {
                            Image(painter = painterResource(id = R.drawable.baseline_person_24),
                                contentDescription = "Icon Person"
                            )},
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface),
                        shape = MaterialTheme.shapes.medium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    TextField(value = "",
                        onValueChange = {},
                        singleLine = true,
                        leadingIcon = {
                            Image(painter = painterResource(id = R.drawable.baseline_lock_24),
                                contentDescription = "Icon Person"
                            )},
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface),
                        shape = MaterialTheme.shapes.medium
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(MaterialTheme.shapes.small),
                        shape = MaterialTheme.shapes.small,
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primaryVariant,
                        )
                    ) {
                        Text(stringResource(id = R.string.login),
                            style = MaterialTheme.typography.h3,
                            color = MaterialTheme.colors.secondary)
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        stringResource(id = R.string.forgotPass),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.secondaryVariant,
                    )

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
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginAppPreview(){
    LoginApp()
}