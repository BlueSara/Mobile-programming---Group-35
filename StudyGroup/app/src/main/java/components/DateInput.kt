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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.window.DialogProperties
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Date-input for selecting time (of day). Shows dialog box for selecting date when input is clicked
 * Component does not hold the date-state itself
 * @param date LocalDate, default: LocalDate.now(), the value to display in the input
 * @param onDate Unit, default: null. returns "LocalDate" when a date is selected
 * @param label String, default: <label>, the text displayed above the input
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInput(
    date: LocalDate? = LocalDate.now(),
    onDate: ((LocalDate) -> Unit)? = null,
    label: String?= "<label>"
){
    val colors = LocalCustomColors.current
    val space = LocalSpacing.current
    var displayPicker by remember { mutableStateOf(false) }
    val currTime = date?: LocalDate.now()
    val dateTime = currTime.atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    val timeState = rememberDatePickerState(
        dateTime
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
                            text="$currTime",
                            fontSize = 18.sp,
                            color = colors.grey
                        )
                        Icon(
                            painter = painterResource(R.drawable.calendaricon),
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
        Dialog(onDismissRequest = {displayPicker = false}, properties = DialogProperties(usePlatformDefaultWidth = false)){
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .padding(vertical = space.m)
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.background)
                        .padding(space.s)
                ){
                    DatePicker(
                        state = timeState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .wrapContentWidth(),
                        colors = DatePickerDefaults.colors(
                            containerColor=colors.background,
                            selectedDayContentColor = colors.white,
                            selectedDayContainerColor = colors.primary
                        ),

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
                            timeState.selectedDateMillis?.let {ms ->
                                val date = Instant.ofEpochMilli(ms)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                onDate?.invoke(date)
                            }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewDateInput(){
    DateInput()
}