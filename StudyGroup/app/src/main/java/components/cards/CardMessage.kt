package components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygroup.R
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing

/**
 * Small card for displaying a "post", used for example in "your posts" screen, or in overview of "chats"
 *
 * "details" MUST be JSON and MUST contain the fields:
 * - time
 * - location
 * - building
 * - room
 * - assistAgreed (Boolean, if "assisting user" has agreed
 * - ownerAgreed (Boolean, if the owner of the
 * - userAgreed (Boolean, if current user has agreed)
 * - isSelected (Boolean, if it has been selected as meetup)
 *
 * @param details Map<String, Any>, default: null. The current post to display
 * @param onClick Unit, default: null. Callback for when button in top right (like/dislike) of card is clicked
 * */
@Composable
fun CardMessage(
    details: Map<String, Any> ?= null,
    onClick: (() -> Unit)?=null,
){

    val colors = LocalCustomColors.current
    val space = LocalSpacing.current

    val data = details ?: mapOf(
        "time" to "28-11-2025, 12:00",
        "location" to "NTNU, Gjøvik",
        "building" to "Amethyst",
        "room" to "A254",
        "comment" to "Some comment here about some stuff that is mentioned",
        "assistAgreed" to false,
        "ownerAgreed" to false,
        "userAgreed" to true,
        "isSelected" to false
    )

    val buttonColor = when(data["userAgreed"]){
        true -> colors.danger
        false -> colors.success
        else -> colors.success
    }

    val textColor = when(data["isSelected"]){
        true -> colors.successActive
        else -> colors.dangerActive
    }

    val approvedColor = when(data["userAgreed"]){
        true -> colors.successActive
        else -> colors.dangerActive
    }

    CardContainer {

        Column(
            modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = space.s,
                start = space.s,
                end = space.s,
                bottom= space.l),
            verticalArrangement = Arrangement.spacedBy(space.xs)
            ){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(){
                    Text(
                        text = "Time:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .width(space.m * 4 + space.xs)
                    )
                    Text(
                        text = "${data["time"]}",
                        fontSize = 18.sp
                    )
                }
                Button(
                    onClick = {onClick?.invoke()},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = colors.white,
                    ),
                    contentPadding = PaddingValues(space.xs),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .width(space.m * 2)
                        .height(space.m * 2)
                ) {
                    if (data["userAgreed"] == true) {
                        Icon(
                            painter = painterResource(R.drawable.dislikeicon),
                            contentDescription = null,
                            modifier = Modifier,
                            tint = colors.white
                        )
                    } else{
                        Icon(
                            painter = painterResource(R.drawable.likeicon),
                            contentDescription = null,
                            modifier = Modifier,
                            tint = colors.white
                        )
                    }
                }
            }
            Row(){
                Text(
                    text = "Location:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .width(space.m * 4 + space.xs)
                )
                Text(
                    text = "${data["location"]}",
                    fontSize = 18.sp
                )
            }
            Row(){
                Text(
                    text = "Room:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .width(space.m * 4 + space.xs)
                )
                Text(
                    text = "${data["building"]}, ${data["room"]}",
                    fontSize = 18.sp
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(top = space.s)
                .fillMaxWidth()
                .background(colors.borderColor)
            ,
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 1.dp)
                    .fillMaxWidth()
                    .background(colors.background)
                ,
            ){
                Column(
                    modifier = Modifier
                        .padding(space.s)
                        .fillMaxWidth()
                        .background(colors.background)
                    ,
                    verticalArrangement = Arrangement.spacedBy(space.s)
                ){
                    Text(
                        text= "${data["comment"] ?: ""}",
                        fontSize = 18.sp,
                        color = colors.grey
                    )
                    Column(){
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                        ){
                            Text(
                                text= "Assistor: ${if (data["assistAgreed"] == true) "Approved" else "Declined"}",
                                fontSize = 18.sp,
                                color = colors.black
                            )
                            Text(
                                text= "Owner: ${if (data["ownerAgreed"] == true) "Approved" else "Declined"}",
                                fontSize = 18.sp,
                                color = colors.black
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                        ){
                            Text(
                                text= "Selected as meetup: ${if (data["isSelected"] == true) "Yes" else "No"}",
                                fontSize = 18.sp,
                                color = textColor
                            )
                            Text(
                                text= "You: ${if (data["userAgreed"] == true) "Approved" else "Declined"}",
                                fontSize = 18.sp,
                                color = approvedColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCardMessage(){
    CardMessage()
}