package screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing
import components.AppButton
import components.ButtonType
import components.TextInput

@Composable
fun SignIn(
    navController: NavHostController ?=null,
){
    val space = LocalSpacing.current
    val colors = LocalCustomColors.current

    var mEmail by remember { mutableStateOf("") }
    var mPassword by remember { mutableStateOf("") }

    fun handleSignIn(){
        if ((mEmail).isEmpty() || (mPassword).isEmpty()) return

        // TODO : include proper logic for checking signin
        val success = true

        if (!success) return
        navController?.navigate("home")
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(space.s, space.m),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Column{
            Text(
                text = "Welcome to Study Group!",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top=space.xl)
            )
            Text(
                text = "Sign in",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = space.xl, bottom = space.l)
            )
            TextInput(
                label = "Email",
                value = mEmail,
                maxLength = 50,
                onChange = {it -> mEmail = it}

            )
            TextInput(label = "Password*",
                maxLength = 150,
                value = { "*".repeat((mPassword).length) }(),
                onChange = {it -> mPassword = it}
            )
            Text(
                text="Already have an account? Sign in here!",
                color = colors.primaryActive,
                textDecoration = TextDecoration.Underline,
                fontSize = 18.sp,
                modifier =  Modifier
                    .padding(top = space.s)
                    .clickable(
                        onClick = {navController?.navigate("signUpCredentials")}
                    )

            )
        }
        AppButton(
            onClick = {handleSignIn()},
            type = ButtonType.PRIMARY,
            text = "Sign In",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignIn(){
    SignIn()
}
