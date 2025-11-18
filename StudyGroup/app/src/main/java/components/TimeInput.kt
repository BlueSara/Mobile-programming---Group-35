package components

import com.example.studygroup.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing
import java.time.LocalTime

/**
 * Time-input for selecting time (of day). Shows dialog box for selecting time when input is clicked
 * Component does not hold the time-state itself
 * @param time LocalTime, default: LocalTime.now(), the value to display in the input
 * @param onSubmit Unit, default: null. returns "int, int" (hour, minute) when a time is selected
 * @param label String, default: <label>, the text displayed above the input
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeInput(
    time: LocalTime? = LocalTime.now(),
    onSubmit: ((Int, Int) -> Unit)? = null,
    label: String?= "<label>"
){
    val colors = LocalCustomColors.current
    val space = LocalSpacing.current

    var displayPicker by remember { mutableStateOf(false) }

    val currTime = time?: LocalTime.now()
    val timeState = rememberTimePickerState(
        initialHour = currTime.hour,
        initialMinute = currTime.minute,
        is24Hour = true
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = space.m)
            .background(colors.background)
    ){
        Box(
            modifier = Modifier
                .drawBehind{
                    drawLine(
                        color = colors.borderColor,
                        start = Offset(x= 0f, y=size.height),
                        end = Offset(x=size.width, y=size.height),
                        strokeWidth = 2.sp.toPx(),
                    )
                }
        ){
            Button(
                onClick = {displayPicker = true},
                modifier = Modifier
                    .height(space.xl)
                    .fillMaxWidth()
                ,
                contentPadding = PaddingValues(vertical = space.xs, horizontal = space.xs),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.background,
                    contentColor = colors.black,
                ),
                shape = RectangleShape,
            ){
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                    ,
                ){
                    Text(text="$label", fontSize = 18.sp )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(space.xs),
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        Text(
                            text="${timeState.hour}:${timeState.minute}",
                            fontSize = 18.sp,
                            color = colors.grey
                        )
                        Icon(
                            painter = painterResource(R.drawable.clockicon),
                            contentDescription = null,
                            modifier = Modifier,
                            tint = colors.black
                        )
                    }
                }
            }
        }
    }

    if (displayPicker){
        Dialog(onDismissRequest = {displayPicker = false}){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                TimePicker(
                    state = timeState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = colors.background,
                        clockDialSelectedContentColor = colors.white,
                        clockDialUnselectedContentColor = colors.black,
                        selectorColor = colors.primary,
                        timeSelectorSelectedContainerColor = colors.background
                    )
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(space.xs)
                ){
                    Button(
                        onClick = { displayPicker = false },
                        modifier = Modifier
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.danger,
                            contentColor = colors.white,
                        ),
                        shape = RoundedCornerShape(4.dp),
                    ){
                        Text(text = "Cancel")
                    }
                    Button(onClick = {
                        onSubmit?.invoke(timeState.hour, timeState.minute)
                        displayPicker = false
                    },
                        modifier = Modifier
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.success,
                            contentColor = colors.white,
                        ),
                        shape = RoundedCornerShape(4.dp),
                    ){
                        Text(text = "Submit")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimeInput(){
    TimeInput()
}