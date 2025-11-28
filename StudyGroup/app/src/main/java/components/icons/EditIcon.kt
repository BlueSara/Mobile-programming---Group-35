package components.icons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
        painter = painterResource(id = R.drawable.editicon),
        contentDescription = "Edit",
        tint = colors.white,
    )

}


@Preview
@Composable
fun PreviewEditIcon() {
    val colors = LocalCustomColors.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.black)
        ,
        contentAlignment = Alignment.Center
    ) {
        EditIcon()
    }
}