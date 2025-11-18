package screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import components.AppButton
import components.ButtonType
import components.DateInput
import components.Select
import components.TextInput
import layout.Footer
import layout.Header
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
    var mAbbreviation by remember { mutableStateOf("") }
    var mExpirationDate by remember { mutableStateOf(LocalDate.now()) }
    var mUseLocation by remember { mutableStateOf(true) }


    Layout(
        navController= navController,
        showFilter = true,
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
                TextInput(
                    value = mTopic,
                    onChange = { mTopic = it },
                    label = "Assignment/topic"
                )
                TextInput(
                    value = mAbbreviation,
                    onChange = { mAbbreviation = it },
                    label = "Study program abbreviation"
                )

                DateInput(
                    date = mExpirationDate,
                    onDate = { mExpirationDate = it },
                    label = "Post expiration"
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        // make entire row clickable so users may tap the label
                        .clickable(
                            onClick = { mUseLocation = !mUseLocation },
                            role = Role.Checkbox
                        )
                ) {
                    Checkbox(
                        checked = mUseLocation,
                        onCheckedChange = { mUseLocation = !mUseLocation },
                        enabled = mUseLocation
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = "Visible only to those close to your location",fontSize = 16.sp)
                }
            }


        },
        footer = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppButton(
                    modifier = Modifier.weight(1f),
                    text = "Cancel",
                    onClick = { /* TODO */ },
                    type = ButtonType.DANGER
                )
                AppButton(
                    modifier = Modifier.weight(1f),
                    text = "Publish",
                    onClick = { /* TODO */ }
                )
            }
        }
    )
}