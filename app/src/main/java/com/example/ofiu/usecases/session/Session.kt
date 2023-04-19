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
                .clip(RoundedCornerShape(bottomStart = 70.dp, bottomEnd = 70.dp))
                .background(MaterialTheme.colors.background)
        )
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, start = 60.dp, end = 60.dp),
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
    }
}

@Composable
fun SessionCard(){
    Surface(
        Modifier
            .padding(30.dp)
            .background(MaterialTheme.colors.onPrimary).width(300.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.large
    ) {
        Column {
            Text(text = stringResource(R.string.welcome),
            style = MaterialTheme.typography.h2,
            textAlign = TextAlign.Center)
            Button(onClick = { /*TODO*/ }) {
                Text(stringResource(id = R.string.login))
            }

        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SessionAppPreview(){
    SessionCard()
}