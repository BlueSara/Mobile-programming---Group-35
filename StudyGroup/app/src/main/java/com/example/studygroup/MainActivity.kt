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
import components.cards.CardMessage
import components.cards.CardSmall
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

    val data1 = mapOf(
        "title" to "The issue that the student has posted, in which they'd appreciate if someone could help them",
        "topic" to "Assignment 1",
        "subjectCode" to "Prog2007",
        "subject" to "Mobile programming",
        "expirationDate" to "29-11-2025",
        "response" to "ditto",
    )

    val data2 = mapOf(
        "title" to "The issue that the student has posted, in which they'd appreciate if someone could help them",
        "topic" to "Assignment 1",
        "subjectCode" to "Prog2007",
        "subject" to "Mobile programming",
        "expirationDate" to "29-11-2025",
        "response" to "skip",
    )

    val data3 = mapOf(
        "title" to "The issue that the student has posted, in which they'd appreciate if someone could help them",
        "topic" to "Assignment 1",
        "subjectCode" to "Prog2007",
        "subject" to "Mobile programming",
        "expirationDate" to "29-11-2025",
        "response" to "assist",
    )

    val data4 = mapOf(
        "messageID" to "someID",
        "groupID" to "someGroupID",
        "postID" to "somePostI",
        "time" to "28-11-2025, 12:00",
        "location" to "NTNU, Gjøvik",
        "building" to "Amethyst",
        "room" to "A254",
        "comment" to "Some comment here about some stuff that is mentioned",
        "assistAgreed" to true,
        "ownerAgreed" to true,
        "userAgreed" to true,
        "isSelected" to true
    )

    val data5 = mapOf(
        "messageID" to "someID",
        "groupID" to "someGroupID",
        "postID" to "somePostI",
        "time" to "28-11-2025, 12:00",
        "location" to "NTNU, Gjøvik",
        "building" to "Amethyst",
        "room" to "A254",
        "comment" to "Some comment here about some stuff that is mentioned",
        "assistAgreed" to false,
        "ownerAgreed" to true,
        "userAgreed" to false,
        "isSelected" to false
    )

    val data6 = mapOf(
        "messageID" to "someID",
        "groupID" to "someGroupID",
        "postID" to "somePostI",
        "time" to "28-11-2025, 12:00",
        "location" to "NTNU, Gjøvik",
        "building" to "Amethyst",
        "room" to "A254",
        "comment" to "Some comment here about some stuff that is mentioned",
        "assistAgreed" to true,
        "ownerAgreed" to false,
        "userAgreed" to true,
        "isSelected" to false
    )

    val data7 = mapOf(
        "messageID" to "someID",
        "groupID" to "someGroupID",
        "postID" to "somePostI",
        "time" to "28-11-2025, 12:00",
        "location" to "NTNU, Gjøvik",
        "building" to "Amethyst",
        "room" to "A254",
        "comment" to "Some comment here about some stuff that is mentioned",
        "assistAgreed" to true,
        "ownerAgreed" to true,
        "userAgreed" to true,
        "isSelected" to false
    )

    Layout(
        navController = navController,
        arrowBack = false,
        content = {
            CardMessage(data4)
            CardMessage(data5)
            CardMessage(data6)
            CardMessage(data7)

            CardSmall(post = data1)
            CardSmall(post = data2)
            CardSmall(post = data3)

            CardSmall(post = data1, showResponse = true)
            CardSmall(post = data2, showResponse = true)
            CardSmall(post = data3, showResponse = true)

            CardLarge(post = data1)
            CardLarge(post = data2)
            CardLarge(post = data2)
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