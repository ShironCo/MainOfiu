package com.example.ofiu.usecases.session

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ofiu.R


@Composable
fun SessionApp(){
    Entry()
    }

@Composable
fun Entry(modifier: Modifier = Modifier){
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
                .padding(top = 30.dp, start = 30.dp, end = 30.dp, bottom = 20.dp),
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
                .height(400.dp),
                elevation = 8.dp,
                shape = RoundedCornerShape(30.dp),
                color = MaterialTheme.colors.onPrimary
            ){
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(stringResource(id = R.string.welcome),
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.secondaryVariant)
                    Button(onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primaryVariant
                        )
                    ) {
                        Text(stringResource(id = R.string.login),
                        style = MaterialTheme.typography.h3)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SessionAppPreview(){
    Entry()
}