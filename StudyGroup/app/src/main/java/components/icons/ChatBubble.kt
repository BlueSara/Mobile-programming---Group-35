package components.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.studygroup.R
import com.example.studygroup.ui.theme.LocalCustomColors


/**
 * ChatBubble
 */
@Composable
fun ChatBubble(){
    val colors = LocalCustomColors.current
    Icon(
        painter = painterResource(id = R.drawable.likeicon),
        contentDescription = "Chat",
        tint = colors.black,
    )

}
