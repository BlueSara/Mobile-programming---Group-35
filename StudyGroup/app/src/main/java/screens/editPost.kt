package screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import layout.Layout

@Composable
fun EditPost(
    navController: NavHostController ?=null,
    postID: String?= null,
    ){
    Layout(
        navController= navController,
        arrowBack = true,
    ) {  }
}