package screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import layout.Layout

@Composable
fun UserReplies(
    navController: NavHostController ?=null,){
    Layout(
        navController= navController,
        arrowBack = true,
    ) {  }
}