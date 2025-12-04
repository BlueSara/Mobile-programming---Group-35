package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing
import components.AppButton
import components.ButtonType
import components.cards.CardMessage
import handleApiReqGet
import handleApiReqPatch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import layout.Layout

/**The screen for viewing a single groups meetup "chat"
 * @param navController - NavHostController, default: null. The navHost controller used to navigate to different screens.
 * @param groupID - String, default: null. The id of the current group to get data for
 * */
@Composable
fun Meetup(
    navController: NavHostController ?=null,
    groupID: String? =null,
    ){

    val mMessages = remember { mutableStateListOf<Map<String, Any>>() }
    val mGroup = remember { mutableStateMapOf<String, Any>() }
    val space = LocalSpacing.current

    val context = LocalContext.current

    /**Fetches the data for this group - group info and messages to display
     * */
    fun handleFetchData() {
        CoroutineScope(Dispatchers.IO).launch{
            //call api handler function to get group info
            val messageResponse = handleApiReqGet("/groups/$groupID", context)
            // Return if err
            //TODO : implement functionality to show pop-up msg to user if any errs occurred
            if (!messageResponse.ok) return@launch

            //parsing content from api response
            val content= messageResponse.content as? Map<*, *> ?: emptyMap<String, Any?>()
            val group = content["groupInfo"] as? Map<String, Any>?: emptyMap()
            val messages = content["messages"] as? List<Map<String, Any>>?: listOf(emptyMap())

            //only show messages if not empty, and not "empty" array
            if (messages.isNotEmpty() && messages.toString() != "[{}]") mMessages.addAll(messages)
            if (group.isNotEmpty()) mGroup.putAll(group)
        }
    }
    /**Handles calling api-handler for updating reply, as well as updating ui when api req is ok.
     * @param id - String, no default: The id of the message to update
     * */
    fun handleUpdateResponse(id : String) {
        CoroutineScope(Dispatchers.IO).launch {
            //finding the current message
            val i = mMessages.indexOfFirst { it -> it["messageID"] == id }
            if (i == -1) return@launch
            val message = mMessages[i].toMutableMap()

            //gets current answer: If it currently is "true" then it should be updated to false
            //if currently not true, e.g. false or null, then it is updated to true
            val answer = mapOf(
                "accept" to when(message["userAgreed"]){
                    true -> false
                    else -> true
                }
            )
            //making the request to api via api handler function
            val response = handleApiReqPatch("/groups/$groupID/messages/$id", context, answer)
            val success = response.ok

            //return if not success
            if (!success) return@launch

            //update message for ui
            message["userAgreed"] = when( message["userAgreed"]) {
                true -> false
                else -> true
            }
            //update item in mutable map for ui to update
            mMessages[i] = message
        }
    }

    handleFetchData()

     Layout(
        navController = navController,
        arrowBack = true,
        showFilter = true,
        pageDetails = {
            MeetupHeader(
                participants = (mGroup["Participants"] as? List<*>)?.size ?: 0,
                helpers = (mGroup["AssistingUsers"] as? List<*>)?.size ?: 0
                )
                  },
        footer = {
            Row( modifier = Modifier.fillMaxWidth())
            {
                AppButton(
                    modifier = Modifier
                        .weight(1f),
                    type = ButtonType.PRIMARY,
                    text = "Make a suggestion",
                    onClick =  {navController?.navigate("createMeetup/$groupID")},
            )}},
        content = {
            // Scrollable main content
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(space.m),
            ) {

                if (mMessages.isNotEmpty()){
                    mMessages.forEach { message ->
                        CardMessage(
                            details = message,
                            onClick = {
                                handleUpdateResponse(message["messageID"]?.toString()?: "")
                            }
                        )
                    }
                }
            }
        }
    )
}
@Composable
fun MeetupHeader(
    participants: Int,
    helpers: Int,
) {
    val space = LocalSpacing.current
    val colors = LocalCustomColors.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(bottom = space.xs)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = colors.borderColor,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }
            .padding(horizontal = space.s, vertical = space.s)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
    }
        Column(verticalArrangement = Arrangement.spacedBy(space.xs)) {
            Text(
                text = "Participants: $participants",
                fontSize = 18.sp,
                color = colors.grey
                )
            Text(
                text = "Helpers:        $helpers",
                fontSize = 18.sp,
                color = colors.grey
            )

        }
    }
}


@Preview(showBackground = true)
@Composable
fun previewMeetup() {
    Meetup()
}
