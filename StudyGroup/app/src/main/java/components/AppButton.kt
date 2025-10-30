package components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing


enum class ButtonType {
    PRIMARY,
    DANGER,
    SUCCESS,
    INFO
}
@Composable
fun AppButton(
    type: ButtonType = ButtonType.PRIMARY,
    onclick: () -> Unit = {},
    text: String = "",
    shape: RoundedCornerShape = RoundedCornerShape(4.dp),
    fullWidth: Boolean = false,
    width: Dp = Dp.Unspecified
) {
    val colors = LocalCustomColors.current
    val space = LocalSpacing.current

    // Background color for button based on enum type
    val backGroundColor = when (type) {
        ButtonType.PRIMARY -> colors.primary
        ButtonType.DANGER -> colors.danger
        ButtonType.SUCCESS -> colors.success
        ButtonType.INFO -> colors.info
    }

    // Modifier setup ( Fullscreen width or specific width)
    val modifier = when {
        fullWidth -> Modifier.fillMaxWidth()
        width != Dp.Unspecified -> Modifier
        else -> Modifier
    }

    // Button template for functionality
    Button(
        onClick = onclick,
        modifier = modifier,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = backGroundColor,
            contentColor = colors.white
        )
    ) {
        Text(
            text = text
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewAppButton() {
    AppButton(
        type = ButtonType.PRIMARY,
        text = "Click Please",
        fullWidth = true,
        onclick = { }
    )
}