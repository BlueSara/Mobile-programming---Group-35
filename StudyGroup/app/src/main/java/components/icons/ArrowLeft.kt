package components.icons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.studygroup.R
import com.example.studygroup.ui.theme.LocalCustomColors

/**
 * ArrowLeft
 */
@Composable
fun ArrowLeft(){
    val colors = LocalCustomColors.current
    Icon(
        painter = painterResource(id = R.drawable.arrowleft),
        contentDescription = "Go Back",
        tint = colors.black,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewArrowLeft() {
    Box(
        modifier = Modifier
            .fillMaxSize()
        ,
        contentAlignment = Alignment.Center
    ) {
        ArrowLeft()
    }
}
