
package navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import screens.AllMeetups
import screens.CreateMeetup
import screens.CreatePost
import screens.EditPost
import screens.Home
import screens.Meetup
import screens.Signin
import screens.SignupCredentials
import screens.SignupUniversity
import screens.UserAccount
import screens.UserPosts
import screens.UserReplies

@Composable
fun AppNavHost(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home"){
        composable("allMeetups"){
            AllMeetups(navController = navController)
        }
        composable("createMeetup"){
            CreateMeetup(navController = navController)
        }
        composable("createPost"){
            CreatePost(navController = navController)
        }
        composable("editPost"){
            EditPost(navController = navController)
        }
        composable("home"){
            Home(navController = navController)
        }
        composable("meetup"){
            Meetup(navController = navController)
        }
        composable("signIn"){
            Signin(navController = navController)
        }
        composable("signUpCredentials"){
            SignupCredentials(navController = navController)
        }
        composable("signUpUniversity"){
            SignupUniversity(navController = navController)
        }
        composable("userAccount"){
            UserAccount(navController = navController)
        }
        composable("userPost"){
            UserPosts(navController = navController)
        }
        composable("userReplies"){
            UserReplies(navController = navController)
        }
    }
}