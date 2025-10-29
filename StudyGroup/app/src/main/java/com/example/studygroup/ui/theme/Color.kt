package com.example.studygroup.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

//Class for custom colors for application
data class CustomColors(
    val primary: Color = Color(0xFF447A9C),
    val primaryActive: Color = Color(0xFF325971),
    val danger: Color = Color(0xFFE91624),
    val dangerActive: Color = Color(0xFFBB111D),
    val success: Color = Color(0xFF458258),
    val successActive: Color = Color(0xFF3A5F45),
    val foreground: Color = Color(0xFFF9F2E2),
    val foreGroundActive: Color = Color(0xFFEBD9A8),
    val info: Color = Color(0xFFD3E8EE),
    val infoActive: Color = Color(0xFF79BBCD),
    val background: Color = Color(0xFFFDFDFC),
    val borderColor: Color = Color(0xFFC4C4C4),
    val grey: Color = Color(0xFF757575),
    val black: Color = Color(0xFF000000),
    val white: Color = Color(0xFFFFFFFF)
)

//variable containing a static composition of the custom colors
val LocalCustomColors = staticCompositionLocalOf {CustomColors()}