package components.icons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.studygroup.R
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment


/**
 * EditIcon
 */
@Composable
fun EditIcon(
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    size: Dp = 24.dp
){
    Icon(
        painter = painterResource(id = R.drawable.likeicon),
        contentDescription = "Edit",
        tint = tint,
        modifier = modifier.size(size)
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
            EditIcon(tint = Color.Black, size = 20.dp)
            Spacer(modifier = Modifier.height(8.dp))
            ChatBubble(tint = Color.Black, size = 30.dp)
            Spacer(modifier = Modifier.height(8.dp))
            ArrowLeft(tint = Color.Black, size = 40.dp)
            Spacer(modifier = Modifier.height(8.dp))
            ArrowRight(tint = Color.Black, size = 50.dp)
        }
    }
}