package screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import layout.Layout

@Composable
fun Home(
    navController: NavHostController ?=null,){
    Layout(
        navController= navController,
    ) {  }
}