package com.example.studygroup.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val xs: Dp = 6.299.dp,
    val s: Dp = 12.598.dp,
    val m: Dp = 18.898.dp,
    val l: Dp = 25.197.dp,
    val xl: Dp = 37.795.dp
)
val LocalSpacing = staticCompositionLocalOf { Spacing() }