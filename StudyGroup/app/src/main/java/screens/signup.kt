package screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import components.AppButton
import components.ButtonType
import components.DateInput
import components.DropDown
import components.PopUp
import components.Select
import components.TextInput
import layout.Footer
import java.time.LocalDate

@Composable
//@Preview(showBackground = true)
fun SignupCredentials(
    navController: NavHostController ?=null)
{
    var mFirstname by remember { mutableStateOf("") }
    var mLastname by remember { mutableStateOf("") }
    var mUnimail by remember { mutableStateOf("") }
    var mPassword by remember { mutableStateOf("") }
    var mPasswordConfirmation by remember { mutableStateOf("") }
    var mAffiliationID by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Welcome to Study Group!",
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Sign up",
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 40.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
        TextInput(
            value = mFirstname,
            onChange = { mFirstname = it },
            label = "First name"
        )
        TextInput(
            value = mLastname,
            onChange = { mLastname = it },
            label = "Last name"
        )
        TextInput(
            value = mUnimail,
            onChange = { mUnimail = it },
            label = "University email"
        )
        TextInput(
            value = { "*".repeat(mPassword.length) }(),
            onChange = { mPassword = it },
            label = "Password",
            maxLength = 100
        )
        TextInput(
            value = { "*".repeat(mPasswordConfirmation.length) }(),
            onChange = { mPasswordConfirmation = it },
            label = "Confirm password",
            maxLength = 100
        )

        Text(
            text = "Choose affiliation",
            style = TextStyle(
                fontSize = 20.sp,
            ),
            modifier = Modifier
                .padding(top = 24.dp)
        )
        Select(
            options = listOf(   // TODO: CHANGE THIS TO BE REAL DATA
                mapOf(
                    Pair("id", "1234"),
                    Pair("name", "NTNU")
                ),
                mapOf(
                    Pair("name", "Oslo Met"),
                    Pair("id", "67")
                ),
                mapOf(
                    Pair("name", "Høyskolen Kristiania"),
                    Pair("id", "9876")
                )
            )
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            AppButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "NEXT",
                /* TODO:   onClick =... */
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SignupUniversity(
    navController: NavHostController ?=null)
{
    var mStudyProgram by remember { mutableStateOf("") }
    var mAbbreviation by remember { mutableStateOf("") }
    var mDateStarted by remember {mutableStateOf(LocalDate.now()) }
    var mGraduation by remember {mutableStateOf(LocalDate.now()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Welcome to Study Group!",
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Sign up",
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 40.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
        TextInput(
            value = mStudyProgram,
            onChange = { mStudyProgram = it },
            label = "Study program name",
            maxLength = 50
        )
        TextInput(
            value = mAbbreviation,
            onChange = { mAbbreviation = it },
            label = "Study program abbreviation",
            maxLength = 10
        )
        
        // TODO : ADD CHOSEN SUBJECTS

        DateInput(
            date = mDateStarted,
            onDate = { mDateStarted = it },
            label = "When did you start studying?"
        )
        DateInput(
            date = mGraduation,
            onDate = { mGraduation = it },
            label = "When do you graduate?"
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppButton(
                    modifier = Modifier.weight(1f),
                    text = "Go Back",
                    onClick = { /* TODO */ },
                    type = ButtonType.DANGER
                )
                AppButton(
                    modifier = Modifier.weight(1f),
                    text = "Sign Up",
                    onClick = { /* TODO */ }
                )
            }
        }
    }
}