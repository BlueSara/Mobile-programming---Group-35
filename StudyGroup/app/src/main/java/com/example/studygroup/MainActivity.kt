package com.example.studygroup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studygroup.ui.theme.StudyGroupTheme
import layout.Layout
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
    Layout(
        navController = navController,
        arrowBack = true,
        content = {
            SomeComp(title = "Home page")
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