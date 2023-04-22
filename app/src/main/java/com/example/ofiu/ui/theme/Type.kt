package com.example.ofiu.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ofiu.R

// Set of Material typography styles to start with
val Monserrat = FontFamily(
    Font(R.font.montserrat_black),
    Font(R.font.montserrat_bold),
    Font(R.font.montserrat_light),
    Font(R.font.montserrat_extrabold),
    Font(R.font.montserrat_medium),
    Font(R.font.montserrat_extralight),
    Font(R.font.montserrat_regular),
    Font(R.font.montserrat_thin),
)
val Roboto = FontFamily(
    Font(R.font.roboto_black),
    Font(R.font.roboto_bold),
    Font(R.font.roboto_light),
    Font(R.font.roboto_medium),
    Font(R.font.roboto_regular),
    Font(R.font.roboto_thin),
)

val Typography = Typography(
    h1 = TextStyle(
        fontFamily = Monserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp
    ),
    h2 = TextStyle(
        fontFamily = Monserrat,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    h3 = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    ),
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)