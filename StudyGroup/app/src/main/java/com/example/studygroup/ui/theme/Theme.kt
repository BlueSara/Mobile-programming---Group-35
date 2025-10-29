package com.example.studygroup.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)


@Composable
fun StudyGroupTheme(
    content: @Composable () -> Unit
) {

    //wrapping material theme to make custom colors available
    CompositionLocalProvider(
        LocalCustomColors provides CustomColors(),
        LocalSpacing provides Spacing()
    ) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = Typography,
            content = content
        )
    }
}