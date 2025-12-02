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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import layout.Layout
import org.json.JSONObject
import toMap
import kotlin.reflect.typeOf

/**The screen for viewing a single groups meetup "chat"
 * @param navController - NavHostController, default: null. The navHost controller used to navigate to different screens.
 * @param groupID - String, default: null. The id of the current group to get data for
 * */
@Composable
fun Meetup(
    navController: NavHostController ?=null,
    groupID: String? =null,
    ){

    var mMessages = remember { mutableStateListOf<Map<String, Any>>() }
    var mGroup = remember { mutableStateMapOf<String, Any>() }
    val space = LocalSpacing.current

    val context = LocalContext.current

    fun handleFetchData() {
        CoroutineScope(Dispatchers.IO).launch{
            val messageResponse = handleApiReqGet("/groups/$groupID", context)
            if (!messageResponse.ok) return@launch
            val content= messageResponse.content as? Map<*, *> ?: emptyMap<String, Any?>()
            val group = content["groupInfo"] as? Map<String, Any>?: emptyMap()
            val messages = content["messages"] as? List<Map<String, Any>>?: listOf(emptyMap())

            mMessages.addAll(messages)
            mGroup.putAll(group)
        }
    }



    fun handleUpdateResponse(id : String) {
        val success = true

        val i = mMessages.indexOfFirst { it -> it["messageID"] == id }
        if (i == -1 || !success) return

        val message = mMessages[i].toMutableMap()

        message["userAgreed"] = when( message["userAgreed"]) {
            true -> false
            else -> true
        }

        mMessages[i] = message
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
                    onClick =  {navController?.navigate("createMeetup")},
            )}},
        content = {
            // Scrollable main content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = space.s),
                verticalArrangement = Arrangement.spacedBy(space.m),
            ) {
                // TODO: Have to use real data
                mMessages.forEach { post ->
                    CardMessage(
                        details = post,
                        onClick = {
                            handleUpdateResponse(post["messageID"]?.toString()?: "")
                        }
                    )
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
