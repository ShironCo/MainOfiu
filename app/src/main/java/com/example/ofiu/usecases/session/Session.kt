package com.example.ofiu.usecases.session

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun SessionApp(){
    Entry()
    }

@Composable
fun Entry(modifier: Modifier = Modifier){
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Yellow).padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .background(Color.Blue)
                .fillMaxWidth().height(400.dp)
                .clip(RoundedCornerShape(20.dp))
        )
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SessionAppPreview(){
    SessionApp()
}