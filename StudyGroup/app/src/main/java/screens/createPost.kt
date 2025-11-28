package screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.studygroup.ui.theme.LocalSpacing
import components.AppButton
import components.ButtonType
import components.DateInput
import components.PageHeadline
import components.Select
import components.TextArea
import layout.Layout
import java.time.LocalDate


val mockUpSubjects:List<Map<String,String>> =  listOf(
    mapOf(
        Pair("id", "1234"),
        Pair("more_data", "what"),
        Pair("name", "NTNU")
    ),
    mapOf(
        Pair("name", "Oslo Met"),
        Pair("id", "67")
    ),
    mapOf(
        Pair("name", "Høyskolen Kristiania"),
        Pair("id", "9876"),
        Pair("more_data", "omg"),
        Pair("even_more_data", "no way")
    ),
)

@Composable
@Preview(showBackground = true)
fun CreatePost(
    navController: NavHostController ?=null,) {
    val availableSubjects = mockUpSubjects // TODO: USE REAL DATA
    var mSelectedSubject by remember { mutableStateOf("") }
    var mTopic by remember { mutableStateOf("") }
    var mExpirationDate by remember { mutableStateOf(LocalDate.now()) }
    var mUseLocation by remember { mutableStateOf(true) }

    val space = LocalSpacing.current

    fun handlePushingPost(){
        //TODO : ADD LOGIC TO VALIDATE AND PUBLISH POST (API)
    }


    Layout(
        navController= navController,
        showFilter = true,
        pageDetails = { PageHeadline(text = "Create post") },
        content =  {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Subject", fontSize = 20.sp)
                Select(
                    onChange = { mSelectedSubject = it },
                    options = availableSubjects
                )
                TextArea(
                    value = mTopic,
                    onChange = { mTopic = it },
                    label = "Assignment/topic")

                DateInput(
                    date = mExpirationDate,
                    onDate = { mExpirationDate = it },
                    label = "Post expiration"
                )

                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(space.s),
                    modifier = Modifier
                        .fillMaxWidth()
                        // make entire row clickable so users may tap the label
                        .clickable(
                            onClick = { mUseLocation = !mUseLocation },
                            role = Role.Checkbox
                        )
                ) {

                    Checkbox(
                        modifier = Modifier
                            .width(space.m)
                            .height(space.m),
                        checked = mUseLocation,
                        onCheckedChange = { mUseLocation = !mUseLocation },
                    )

                    Text(text = "Visible only to those close to your location",fontSize = 18.sp)
                }
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
                    onClick = { navController?.navigate("home") },
                    type = ButtonType.DANGER
                )
                AppButton(
                    modifier = Modifier.weight(1f),
                    text = "Publish",
                    onClick = { handlePushingPost() }
                )
            }
        }
    )
}