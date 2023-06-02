package com.example.ofiu.usecases.session

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ofiu.R
import com.example.ofiu.usecases.navigation.AppScreens


@Composable
fun SessionApp(navController: NavController){
    Entry(navController)
    }

@Composable
fun Entry(navController: NavController){
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
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)){
            Row(modifier = Modifier
                .wrapContentWidth()
                .padding(top = 20.dp, start = 30.dp, end = 30.dp, bottom = 25.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.splashlogo),
                    contentDescription = "Icon Ofiu",
                    Modifier.weight(1F))
                Image(
                    painter = painterResource(id = R.drawable.nombresplashlogo),
                    contentDescription = "Name Ofiu",
                    Modifier.weight(1F)
                )
            }
            Surface(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
                elevation = 8.dp,
                shape = RoundedCornerShape(30.dp),
                color = MaterialTheme.colors.onPrimary
            ){
                Column(
                    modifier = Modifier
                        .padding(top = 30.dp, start = 20.dp, end = 20.dp, bottom = 30.dp)
                        .wrapContentWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(id = R.string.welcome),
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.secondaryVariant)
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(MaterialTheme.shapes.small),
                        shape = MaterialTheme.shapes.small,
                        onClick = {
                            navController.navigate(AppScreens.Login.route)
                                  },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primaryVariant,
                        )
                    ) {
                        Text(stringResource(id = R.string.login),
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.secondary)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(MaterialTheme.shapes.small),
                        shape = MaterialTheme.shapes.small,
                        border = BorderStroke(2.dp, MaterialTheme.colors.primaryVariant),
                        onClick = {
                            navController.navigate(AppScreens.Legal.route)
                                  },
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = MaterialTheme.colors.onPrimary,
                        )
                    ) {
                        Text(stringResource(id = R.string.register),
                            style = MaterialTheme.typography.h3,
                            color = MaterialTheme.colors.primaryVariant)
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(stringResource(id = R.string.loginapp),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.secondaryVariant)
                    Spacer(modifier = Modifier.height(20.dp))
                    Row() {
                        Image(painter = painterResource(id = R.drawable.google___original),
                          contentDescription = "Logo de Google")
                        Spacer(modifier = Modifier.width(1.dp))
                        Image(painter = painterResource(id = R.drawable.facebook___original),
                            contentDescription = "Logo de Google")
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SessionAppPreview(){
    //Entry()
}