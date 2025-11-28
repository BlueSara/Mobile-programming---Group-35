package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing
import components.AppButton
import components.ButtonType
import components.cards.CardMessage
import layout.Layout





val meetupMockUp = mutableListOf<Map<String, Any>>(
    mapOf(
        "messageID" to "kwknrgwe",
        "time" to "29-11-2025, 12:00",
        "location" to "NTNU Gjøvik",
        "building" to "Amethyst",
        "room" to "A254",
        "assistAgreed" to true,
        "ownerAgreed" to false,
        "userAgreed" to false,
        "isSelected" to false
    ),
    mapOf(
        "messageID" to "jkerngl",
        "time" to "29-11-2025, 10:00",
        "location" to "NTNU Gjøvik",
        "building" to "Amethyst",
        "room" to "A254",
        "assistAgreed" to true,
        "ownerAgreed" to false,
        "userAgreed" to false,
        "isSelected" to false
    ),
    mapOf(
        "messageID" to "hjfewvbhfi",
        "time" to "29-11-2025, 14:00",
        "location" to "NTNU Gjøvik",
        "building" to "Amethyst",
        "room" to "A254",
        "assistAgreed" to true,
        "ownerAgreed" to false,
        "userAgreed" to false,
        "isSelected" to false
    )
)

val mockupGroup: Map<String, Any> = mapOf(
    "assistingUsers" to listOf(
        "dFECXP03rIxMvdortonf",
        "5JYrloQDFRBXJL0RlAHP"
    ),
    "dittoUsers" to listOf(
        "lT7vSpMNrHvRqYhJ0xiT"
    ),
    "messages" to emptyList<Any>(),
    "participants" to listOf(
        "lT7vSpMNrHvRqYhJ0xiT",
        "dFECXP03rIxMvdortonf",
        "5JYrloQDFRBXJL0RlAHP"
    ),
    "postID" to "A7tTGFUUO1JCqqIw1Tph"
)

@Composable
fun Meetup(
    navController: NavHostController ?=null,
    //meetupID: String? =null,
    ){

    var mMessages = remember { mutableStateListOf<Map<String, Any>>() }
    var mGroup = remember { mutableMapOf<String, Any>() }
    val space = LocalSpacing.current

    fun handleFetchData() {

        // TODO: ADD ACTUAL LOGIC TO FETCH DATA VIA API HANDLER
        val success = true

        if (!success) return
        mMessages = meetupMockUp.toMutableStateList()
        mGroup = mockupGroup.toMutableMap()
    }

    fun handleUpdateResponse(id : String) {
        /* TODO :
            ADD LOGIC TO HANDLE IF CURRENT USER IS OWNER OR ASSISTING.
            E.G. FIX SO "ownerAgreed" AND "assistAgreed" CAN BE UPDATED
            AS WELL IF NECESSARY

        *   */
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
                participants = (mGroup["participants"] as? List<*>)?.size?: 0,
                helpers = (mGroup["assistingUsers"] as? List<*>)?.size?: 0
            )},
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
