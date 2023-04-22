package com.example.ofiu.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ofiu.R

// Set of Material typography styles to start with
val Monserrat = FontFamily(
    Font(R.font.montserrat_thin, FontWeight.Thin),
    Font(R.font.montserrat_black, FontWeight.Black),
    Font(R.font.montserrat_bold, FontWeight.Bold),
    Font(R.font.montserrat_light, FontWeight.Light),
    Font(R.font.montserrat_extrabold, FontWeight.ExtraBold),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_extralight, FontWeight.ExtraLight),
    Font(R.font.montserrat_regular, FontWeight.Normal),
)
val Roboto = FontFamily(
    Font(R.font.roboto_black, FontWeight.Black),
    Font(R.font.roboto_bold, FontWeight.Bold),
    Font(R.font.roboto_light, FontWeight.Light),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_thin, FontWeight.Thin),
)

val Typography = Typography(
    h1 = TextStyle(
        fontFamily = Monserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp
    ),
    h2 = TextStyle(
        fontFamily = Monserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    h3 = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    body1 = TextStyle(
        fontFamily = Monserrat,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)