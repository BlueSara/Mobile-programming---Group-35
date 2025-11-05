package components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing

/**
 * text-input, for inputting text in a single line input
 * Component does not hold the text-state itself
 * @param value String, default: "", the value to display in the input
 * @param onChange Unit, default: null. Returns the updated value of the input
 * @param onFocus Unit, default: null. Callback for when input receives focus
 * @param onBlur Unit, default: null. Callback for when input is no longer in focus
 * @param label String, default: <label>, the text displayed above the input
 * @param maxLength Int, default: 30. Max length that the input can have, also the number
 * */
@Composable
fun TextInput(value: String = "",
              onChange: ((String) -> Unit)? = null,
              onFocus:(() -> Unit)? = null,
              onBlur: (() -> Unit)? = null,
              label: String = "<label>",
              maxLength: Int = 30
){
    val colors = LocalCustomColors.current
    val space = LocalSpacing.current

    var focused by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .padding(bottom = space.m)
        ,
        verticalArrangement = Arrangement.spacedBy(space.xs)
    ){
        Text(label, fontSize = 20.sp)
        Row (
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(space.xs),
            modifier = Modifier
                .drawBehind {
                    val strokeWidth = 2.sp.toPx()
                    if (focused) {
                        drawRoundRect(
                            color = colors.primary,
                            size = size.copy(width = size.width, height = size.height),
                            cornerRadius = CornerRadius(8f, 8f),
                            style = Stroke(width = strokeWidth)
                        )
                    }
                }
                .fillMaxWidth()
                .border(width = 1.dp, color = colors.borderColor, RoundedCornerShape(4.dp))
                .clip(RoundedCornerShape(4.dp))
        ){
            BasicTextField(
                value = value,
                onValueChange = { it -> if (it.length <= maxLength) onChange?.invoke(it)},
                textStyle = TextStyle(
                    fontSize = 18.sp,
                ),
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { it ->
                        if (!it.isFocused && focused) onBlur?.invoke()
                        if (!focused && it.isFocused) onFocus?.invoke()
                        focused = it.isFocused
                    }
                    .height(space.xl)
                    .background(colors.background)
                    .padding(space.xs)
                    .padding(top = space.xs / 2),
                )
            Text(
                text= "${value.length}/${maxLength}",
                fontSize = 18.sp,
                color = colors.grey,
                modifier = Modifier
                    .padding(bottom = space.xs, end = space.xs)
                    .background(colors.background)
                )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTextInput(){
    var textValue by remember { mutableStateOf("Some value") }
    TextInput(value = textValue, onChange = {value -> textValue = value})
}