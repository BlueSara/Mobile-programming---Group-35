package components

import android.view.MotionEvent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing


enum class ButtonType {
    PRIMARY,
    PRIMARYACTIVE,
    DANGER,
    DANGERACTIVE,
    SUCCESS,
    SUCCESSACTIVE,
    INFO,
    INFOACTIVE
}
@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.PRIMARY,
    onClick: (() -> Unit )?= null,
    text: String = "",
    shape: RoundedCornerShape = RoundedCornerShape(4.dp),
    width: Dp = Dp.Unspecified
) {
    val colors = LocalCustomColors.current
    val space = LocalSpacing.current

    var pressed by remember { mutableStateOf(false) }

    val groupedModifier = Modifier
        .height(space.xl)
        .pointerInteropFilter {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> pressed = true
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> pressed = false
            }
            false
        }

    // Modifier setup ( Fullscreen width or specific width)
    val customModifier = when {
        width != Dp.Unspecified -> {
            Modifier
                .width(width)
                .then(groupedModifier)

        }
        modifier != Modifier -> {
            Modifier
                .then(groupedModifier)
                .then(modifier)
        }
        else -> {
            Modifier
                .width(space.xl * 5)
                .then(groupedModifier)
        }
    }
    // Background color for button based on enum type
    val backGroundColor = when (pressed) {
        true -> when (type) {
            ButtonType.PRIMARY -> colors.primaryActive
            ButtonType.DANGER -> colors.dangerActive
            ButtonType.SUCCESS -> colors.successActive
            ButtonType.INFO -> colors.infoActive

            ButtonType.PRIMARYACTIVE -> colors.primary
            ButtonType.DANGERACTIVE -> colors.danger
            ButtonType.SUCCESSACTIVE -> colors.success
            ButtonType.INFOACTIVE -> colors.info
        }
        else -> when (type) {
            ButtonType.PRIMARY -> colors.primary
            ButtonType.PRIMARYACTIVE -> colors.primaryActive
            ButtonType.DANGER -> colors.danger
            ButtonType.DANGERACTIVE -> colors.dangerActive
            ButtonType.SUCCESS -> colors.success
            ButtonType.SUCCESSACTIVE -> colors.successActive
            ButtonType.INFO -> colors.info
            ButtonType.INFOACTIVE -> colors.infoActive
        }
    }

    val textColor = when(type) {
        ButtonType.INFO -> colors.black
        ButtonType.INFOACTIVE -> colors.black
        else -> colors.white
    }

    // Button template for functionality
    Button(
        onClick = {
            onClick?.invoke()
            pressed = !pressed
                  },
        modifier = customModifier,
        shape = shape,
        contentPadding = PaddingValues(space.xs),
        colors = ButtonDefaults.buttonColors(
            containerColor = backGroundColor,
            contentColor = textColor
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
    Row {
        AppButton(
            type = ButtonType.INFO,
            text = "Click Please",
            modifier = Modifier.weight(1f)
        )
    }
}