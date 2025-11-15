package screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import components.Select
import components.TextInput
import layout.Footer

@Composable
@Preview(showBackground = true)
fun SignupCredentials(
    navController: NavHostController ?=null)
{
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var unimail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }
    var affiliationID by remember { mutableStateOf("") }
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


        TextInput(
            value = firstname,
            onChange = { firstname = it },
            label = "First name"
        )
        TextInput(
            value = lastname,
            onChange = { lastname = it },
            label = "Last name"
        )
        TextInput(
            value = unimail,
            onChange = { unimail = it },
            label = "University email"
        )
        TextInput(
            value = { "*".repeat(password.length) }(),
            onChange = { password = it },
            label = "Password",
            maxLength = 100
        )
        TextInput(
            value = { "*".repeat(passwordConfirmation.length) }(),
            onChange = { passwordConfirmation = it },
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
            options = listOf(
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
fun SignupUniversity(
    navController: NavHostController ?=null){
}