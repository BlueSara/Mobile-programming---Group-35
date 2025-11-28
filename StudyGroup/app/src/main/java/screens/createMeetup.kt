package screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.studygroup.ui.theme.LocalSpacing
import components.AppButton
import components.ButtonType
import components.DateInput
import components.PageHeadline
import components.TextArea
import components.TextInput
import components.TimeInput
import layout.Layout
import java.time.LocalTime
import java.time.LocalDate

@Composable
@Preview(showBackground = true)
fun CreateMeetup(
    navController: NavHostController ?=null,
    meetupID: String?= null,
    ){

    val space = LocalSpacing.current
    
    Layout(
        navController= navController,
        arrowBack = true,
        pageDetails = {
            PageHeadline(text="Create meetup suggestion")},
        content = {
            val topic: String = "mockup test"
            var mDate by remember { mutableStateOf(LocalDate.now()) }
            var mTime by remember { mutableStateOf(LocalTime.now()) }
            var mLocation by remember { mutableStateOf("") }
            var mBuilding by remember { mutableStateOf("") }
            var mRoom by remember { mutableStateOf("") }
            var mComments by remember { mutableStateOf("") }

            Column {

                Spacer(modifier = Modifier.height(8.dp))
                Text("Topic: $topic",fontSize = 20.sp)

                Spacer(modifier = Modifier.height(16.dp))
                DateInput(
                    date = mDate,
                    onDate = { mDate = it },
                    label = "Date"
                )
                TimeInput(
                    time = mTime,
                    onSubmit = {hour: Int, min: Int ->  mTime = LocalTime.now().withHour(hour).withMinute(min)},
                    label = "Time"
                )

                TextInput(
                    value = mLocation,
                    onChange = {mLocation = it},
                    label = "Location"
                )
                TextInput(
                    value = mBuilding,
                    onChange = {mBuilding = it},
                    label = "Building"
                )
                TextInput(
                    value = mRoom,
                    onChange = {mRoom = it},
                    label = "Room"
                )
                TextArea(
                    value = mComments,
                    onChange = {mComments = it},
                    label = "Comments"
                )
            }
        },
        footer = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space.xs)
            ) {
                AppButton(
                    modifier = Modifier.weight(1f),
                    text = "Cancel",
                    onClick = { /* TODO */ },
                    type = ButtonType.DANGER
                )
                AppButton(
                    modifier = Modifier.weight(1f),
                    text = "Submit",
                    onClick = { /* TODO */ }
                )
            }
        },
    )
}