package components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing

/**
 * text-input, for inputting text in a multiple-line input.
 * @param value String, default: "", the value to display in the input
 * @param onChange Unit, default: null. Returns the updated value of the input
 * @param onFocus Unit, default: null. Callback for when input receives focus
 * @param onBlur Unit, default: null. Callback for when input is no longer in focus
 * @param label String, default: <label>, the text displayed above the input
 * @param maxLength Int, default: 30. Max length that the input can have, also the number
 * */
@Composable
fun TextArea(value: String = "",
              onChange: ((String) -> Unit)? = null,
              onFocus:(() -> Unit)? = null,
              onBlur: (() -> Unit)? = null,
              label: String = "<label>",
              maxLength: Int = 150
){
    val colors = LocalCustomColors.current
    val space = LocalSpacing.current

    val state = remember { TextFieldState(value) }
    var focused by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .padding(bottom = space.m)
        ,
        verticalArrangement = Arrangement.spacedBy(space.xs),
        horizontalAlignment = Alignment.Start
    ){
        Text(label, fontSize = 20.sp)
        Column (
            verticalArrangement = Arrangement.spacedBy(space.xs),
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .drawBehind{
                    val strokeWidth = 2.sp.toPx()
                    if (focused){
                        drawRoundRect(
                            color = colors.primary,
                            size = size.copy(width = size.width, height = size.height),
                            cornerRadius = CornerRadius(8f, 8f),
                            style = Stroke(width = strokeWidth)
                        )
                    }
                }
                .fillMaxWidth()
                .border(width= 1.dp, color = colors.borderColor, RoundedCornerShape(4.dp))
                .clip(RoundedCornerShape(4.dp))
        ){
            BasicTextField(
                state = state,
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged{state ->
                        if (!state.isFocused && focused) onBlur?.invoke()
                        if (!focused && state.isFocused) onFocus?.invoke()
                        focused = state.isFocused
                    }
                    .height(space.xl * 3)
                    .background(colors.background)
                    .padding(space.xs)
                ,
                decorator = {field ->
                    val text = state.text.toString()
                    if (text.length > maxLength){
                        state.edit { replace(0, text.length, text.take(maxLength)) }
                    }
                    field()
                }
            )
            Text(
                text= "${state.text.toString().length}/${maxLength}",
                fontSize = 18.sp,
                color = colors.grey,
                modifier = Modifier
                    .padding(bottom = space.xs, end = space.xs)
                    .background(colors.background)
            )
        }

        LaunchedEffect(state) {
            snapshotFlow { state.text.toString() }
                .collect { newValue ->
                    if (newValue != value ) onChange?.invoke(newValue)
                }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTextArea(){
    TextArea(value = "Some value")
}