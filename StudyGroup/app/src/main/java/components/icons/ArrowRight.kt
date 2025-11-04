package components.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.studygroup.R
import com.example.studygroup.ui.theme.LocalCustomColors

/**
 * ArrowRight
 */
@Composable
fun ArrowRight(){
    val colors = LocalCustomColors.current
    Icon(
        painter = painterResource(id = R.drawable.likeicon),
        contentDescription = "Navigate",
        tint = colors.white,
    )

}