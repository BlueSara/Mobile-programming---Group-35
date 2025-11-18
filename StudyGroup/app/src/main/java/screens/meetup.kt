package screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import layout.Layout

@Composable
fun Meetup(
    navController: NavHostController ?=null,
    meetupID: String? =null,
    ){
    Layout(
        navController= navController,
        arrowBack = true,
    ) {  }
}