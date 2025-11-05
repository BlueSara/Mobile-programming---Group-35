package components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
 * Large card for displaying a "post", used for mainly in "home" screen when viewing posts
 *
 * "post" MUST be JSON and MUST contain the fields:
 * - title
 * - topic
 * - subjectCode
 * - subject
 * - expirationDate
 *
 * @param post Map<String, Any>, default: null. The current post to display
 * */
@Composable
fun CardLarge(post: Map<String, Any> ?= null){

    val colors = LocalCustomColors.current
    val space = LocalSpacing.current

    val data = post ?: mapOf(
        "title" to "The issue that the student has posted, in which they'd appreciate if someone could help them",
        "topic" to "Assignment 1",
        "subjectCode" to "Prog2007",
        "subject" to "Mobile programming",
        "expirationDate" to "29-11-2025"
    )

    CardContainer {
        Row(
            modifier = Modifier
                .background(colors.info)
                .fillMaxWidth()
                .padding(horizontal = space.s, vertical = space.m),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = "${data["subjectCode"]}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = " - ${data["subject"]}", fontWeight = FontWeight.Normal, fontSize = 18.sp)
        }
        Column(
            modifier = Modifier
                .padding(start= 0.dp, end= 0.dp, top = space.xs)
                .defaultMinSize(minHeight = space.xl * 10)
            ,
            verticalArrangement = Arrangement.spacedBy(space.xl * 2)
        ){
            Column(
                modifier = Modifier
                    .padding(start= space.s, end= space.s, top = (space.xl * 2) - space.xs),
                verticalArrangement = Arrangement.spacedBy(space.xl)
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
        Box(
            modifier = Modifier
                .padding(top = space.xl * 2)
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
                Box(
                    modifier = Modifier
                        .padding(space.s)
                        .fillMaxWidth()
                        .background(colors.background)
                    ,
                ){
                    Text(
                        text= "Expires at ${data["expirationDate"]}",
                        fontSize = 18.sp,
                        color = colors.grey
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCardLarge(){
    CardLarge()
}