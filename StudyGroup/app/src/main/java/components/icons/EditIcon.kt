package components.icons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studygroup.R
import androidx.compose.ui.Alignment
import com.example.studygroup.ui.theme.LocalCustomColors


/**
 * EditIcon
 */
@Composable
fun EditIcon(){
    val colors = LocalCustomColors.current
    Icon(
        painter = painterResource(id = R.drawable.likeicon),
        contentDescription = "Edit",
        tint = colors.white,
    )

}


@Preview                                                         
@Composable                                                      
fun PreviewEditIcon() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            EditIcon()
            Spacer(modifier = Modifier.height(8.dp))
            ChatBubble()
            Spacer(modifier = Modifier.height(8.dp))
            ArrowLeft()
            Spacer(modifier = Modifier.height(8.dp))
            ArrowRight()
        }
    }
}