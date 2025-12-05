package components

import android.view.MotionEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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


/**
 * Button types that represents the visual style of the AppButton
 */
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

/**
 * Custom button used throughout the application, typically a "Cancel" or "Done" button or the like.
 *
 * @param modifier Optional layout modifier
 * @param type Determines the visual style of the button
 * @param onClick Callback invoked when the button is pressed
 * @param text Text to display inside the button
 * @param shape Corner shape of the button
 * @param width Width of the button
 */
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
    val space = LocalSpacing.current
    Column (
        verticalArrangement = Arrangement.spacedBy(space = space.xs)
    ){
        Row(
            horizontalArrangement = Arrangement.spacedBy(space.xs)
        ){
            AppButton(
                type = ButtonType.PRIMARY,
                text = "Primary",
                onClick = { },
                modifier = Modifier.weight(1f),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            AppButton(
                type = ButtonType.DANGER,
                text = "Danger",
                onClick = { }
            )}
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AppButton(
                type = ButtonType.SUCCESS,
                text = "Success",
                onClick = { }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AppButton(
                type = ButtonType.INFO,
                text = "Info",
                onClick = { }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AppButton(
                type = ButtonType.PRIMARYACTIVE,
                text = "Primary Active",
                onClick = { }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AppButton(
                type = ButtonType.DANGERACTIVE,
                text = "Danger Active",
                onClick = { }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AppButton(
                type = ButtonType.SUCCESSACTIVE,
                text = "Success Active",
                onClick = { }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AppButton(
                type = ButtonType.INFOACTIVE,
                text = "Info Active",
                onClick = { }
            )
        }
    }
}