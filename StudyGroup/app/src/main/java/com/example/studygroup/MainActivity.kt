package com.example.studygroup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studygroup.ui.theme.StudyGroupTheme
import components.DateInput
import components.TextArea
import components.TextInput
import components.TimeInput
import components.cards.CardContainer
import components.cards.CardLarge
import layout.Layout
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyGroupTheme {
                AppNavHost()
            }
        }
    }
}

@Composable
fun AppNavHost(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home"){
        composable("home"){
            HomeScreen(navController = navController)
        }
        composable("other"){
            OtherScreen(navController = navController)
        }
    }

}

@Composable
fun SomeComp(title: String = "Some text here"){
    Text(text= title)
}

@Composable
fun HomeScreen(navController: NavHostController){
    var firstname by remember { mutableStateOf("John") }
    var lastname by remember { mutableStateOf("Doe") }
    var description by remember { mutableStateOf("Doe") }
    var date by remember { mutableStateOf(LocalDate.now()) }

    Layout(
        navController = navController,
        arrowBack = false,
        content = {
            /*
            TextInput(
                label = "First name",
                value = firstname,
                onChange = { newVal -> firstname = newVal },
                maxLength = 30
            )
            TextInput(
                label = "Last name",
                value = lastname,
                onChange = { newVal -> lastname = newVal },
                maxLength = 30
            )
            TextArea(
                label = "Description",
                value = description,
                onChange = { newVal -> description = newVal },
            )
            TimeInput()
            DateInput(onDate = {newDate -> date = newDate}, date = date)*/
            CardLarge()
        })
}

@Composable
fun OtherScreen(navController: NavHostController){
    Layout(
        navController = navController,
        arrowBack = false,
        content = { SomeComp(title = "Other page") })
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StudyGroupTheme {
        Layout(
            content = {
                SomeComp()
                SomeComp()
            }
        ){

        }
    }
}