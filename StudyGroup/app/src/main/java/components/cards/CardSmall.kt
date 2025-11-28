package components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing

/**
 * Small card for displaying a "post", used for example in "your posts" screen, or in overview of "chats"
 *
 * "post" MUST be JSON and MUST contain the fields:
 * - title
 * - topic
 * - subjectCode
 * - subject
 * - expirationDate
 *
 * @param post Map<String, Any>, default: null. The current post to display
 * @param onClick Unit, default: null. Callback for when button in top right of card is clicked
 * @param showResponse Boolean, default: false. Boolean for handling if currents user response show be displayed next to top right button or not.
 * @param icon Composable, Default: null. The icon to display in the top-right button (for example edit or arrow icon)
 * */
@Composable
fun CardSmall(
    post: Map<String, Any> ?= null,
    onClick: (() -> Unit)?=null,
    showResponse: Boolean = false,
    icon: (@Composable (() -> Unit))? =null
    ){

    val colors = LocalCustomColors.current
    val space = LocalSpacing.current

    val data = post ?: mapOf(
        "title" to "The issue that the student has posted, in which they'd appreciate if someone could help them",
        "topic" to "Assignment 1",
        "subjectCode" to "Prog2007",
        "subject" to "Mobile programming",
        "expirationDate" to "29-11-2025",
        "response" to "ditto",
    )

    val borderColor = when(data["response"].toString().lowercase()){
        "ditto" -> colors.primary
        "assist" -> colors.success
        "skip" -> colors.danger
        else -> colors.grey
    }

        CardContainer {
        Row(
            modifier = Modifier
                .background(colors.info)
                .fillMaxWidth()
                .padding(space.s),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${data["subjectCode"]}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(space.xs)
            ){
                if (showResponse && data["response"] != null){
                    Column(
                        modifier = Modifier
                            .border(width = space.xs, color = borderColor, shape = RoundedCornerShape(4.dp))
                            .height(space.m * 2)
                            .width(space.xl * 2),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text="${data["response"] ?: "Null"}", fontSize = 18.sp)
                    }
                }
                Button(
                    onClick = {onClick?.invoke()},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.white,
                    ),
                    contentPadding = PaddingValues(space.xs),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .width(space.m * 2)
                        .height(space.m * 2)
                ) {
                    if (icon != null) icon()
                    else Text(text="Go")
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(start= space.s, end= space.s, top = space.l - space.xs, bottom= space.m),
            verticalArrangement = Arrangement.spacedBy(space.m)
        ){
            Text(
                text= "${data["topic"]}",
                style = TextStyle(
                    textDecoration = TextDecoration.Underline
                ),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Text(
                text= "${data["title"]}",
                fontSize = 22.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCardSmall(){
    CardSmall()
}