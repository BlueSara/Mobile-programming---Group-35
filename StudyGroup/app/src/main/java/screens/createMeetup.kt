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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing
import components.AppButton
import components.ButtonType
import components.DateInput
import components.PageHeadline
import components.PopUp
import components.TextArea
import components.TextInput
import components.TimeInput
import handleApiReqGet
import handleApiReqPost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import layout.Layout
import java.time.LocalTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
@Preview(showBackground = true)
fun CreateMeetup(
    navController: NavHostController ?=null,
    groupID: String?= null,
    ){

    val space = LocalSpacing.current
    val colors = LocalCustomColors.current
    val context = LocalContext.current


    var topic by remember { mutableStateOf("") }
    var mDate by remember { mutableStateOf(LocalDate.now()) }
    var mTime by remember { mutableStateOf(LocalTime.now()) }
    var mLocation by remember { mutableStateOf("") }
    var mBuilding by remember { mutableStateOf("") }
    var mRoom by remember { mutableStateOf("") }
    var mComments by remember { mutableStateOf("") }
    var mErrMessage by remember { mutableStateOf("") }

    /**Function for getting group-information and
     * get topic for the post this group is relevant to
     * */
    fun handleGetGroup(){
        CoroutineScope(Dispatchers.IO).launch {
            val response = handleApiReqGet("/post/groups", context)
            if (!response.ok) {
                //TODO : HANDLE TO SHOW ERR IF ERR WAS RETURNED
                return@launch
            }
            val groups = response.content as List<Map<String,Any>>

            //find the specific group within the groups returned by groupID
            val group = groups.find { it -> it["groupID"] == groupID }
            topic = (group?.get("topic")?: "") as String
        }
    }

    handleGetGroup()

    /**
     * Handles logic for calling api-handler to create new suggested meetup-time
     * */
    fun handleSubmitSuggestion(){
        val fullTime = LocalDateTime.of(mDate, mTime)
        val fullTimeZone = fullTime.atZone(ZoneOffset.UTC)
        val formattedTime = fullTimeZone.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))

        //handle to show error popup if
        if (fullTime.isBefore(LocalDateTime.now()) || mLocation.isEmpty()) {
            mErrMessage = "Either location is empty, or selected time is before current time."
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val content: Map<String, String> = mapOf(
                "groupID" to groupID.toString(),
                "time" to formattedTime,
                "location" to mLocation,
                "building" to mBuilding,
                "room" to mRoom,
                "comment" to mComments,
            )

            val response = handleApiReqPost("/groups/${groupID.toString()}", content, context)
            if (!response.ok) {
                mErrMessage = "An error occurred while completing the request"
                return@launch
            }
        }

        navController?.popBackStack()
    }

    
    Layout(
        navController= navController,
        arrowBack = true,
        pageDetails = {
            PageHeadline(text="Create meetup suggestion")},
        content = {
            Column {

                if (!mErrMessage.isEmpty()){
                    PopUp(
                        title = "Error",
                        onDismiss = {mErrMessage = ""}
                    ){
                        Text(mErrMessage, fontSize = 18.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Topic: $topic",fontSize = 20.sp, color = colors.grey)

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
                    onClick = { navController?.popBackStack() },
                    type = ButtonType.DANGER
                )
                AppButton(
                    modifier = Modifier.weight(1f),
                    text = "Submit",
                    onClick = { handleSubmitSuggestion() }
                )
            }
        },
    )
}