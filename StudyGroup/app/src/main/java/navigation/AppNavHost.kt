
package navigation

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dataLayer.api.getToken
import screens.AllGroups
import screens.CreateMeetup
import screens.CreatePost
import screens.EditPost
import screens.Home
import screens.SignIn
import screens.SignupCredentials
import screens.SignupUniversity
import screens.SingleGroup
import screens.UserAccount
import screens.UserReplies
import screens.UserPosts


/**For handling protected screens
 * @param route - String, no default: The route to the screen to display
 * @param isSignedIn - Boolean, no default. State for if user is signed in or not.
 * @param navController - NavHostController, no default. For auto-navigating user to sign-in screen
 * if not signed in and attempting to view screen requiring sign-in
 * @param content - Composable, no default. The composable screen to display when user is signed in
 * */
fun NavGraphBuilder.signedInScreen(
    route: String,
    isSignedIn: Boolean,
    navController: NavHostController,
    content : @Composable (NavBackStackEntry) -> Unit,
){
    val routeHasId = route.contains("{id}")
    val args = when (routeHasId){
        true -> listOf(navArgument("id") {type = NavType.StringType})
        else -> emptyList()

    }

    composable(route,
        arguments = args
        ) { backStackEntry ->
        if (isSignedIn){
            content(backStackEntry)
        } else{
            LaunchedEffect(Unit) {
                navController.navigate("signIn"){
                    popUpTo(route) {inclusive = true}
                }
            }
        }
    }
}
/**The navigation host for the application.
 * */
@Composable
fun AppNavHost(){
    val navController = rememberNavController()
    val context = LocalContext.current

    //The api_token for tracking if user is signed in or not
    var token by remember { mutableStateOf(getToken(context)) }

    //boolean handling state for if user is signed in or not
    var mIsSignedIn by remember { mutableStateOf(token != null) }

    // start destination for which screen user initially should view
    var startDestination by remember { mutableStateOf(if (mIsSignedIn) "home" else "signIn") }

    //checking changes to the token stored in preferences.
    // If token is updated, meaning the user has signed in/out, the
    // allowed screens and start-destination is updated to correspond with signed-in status
    DisposableEffect(context) {
        val preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val listener = SharedPreferences.OnSharedPreferenceChangeListener{_, key ->
            if(key == "api_token") {
                token = preferences.getString("api_token", null)
                mIsSignedIn = token != null
                if (mIsSignedIn) startDestination = "home"
            }
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        onDispose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    NavHost(navController = navController, startDestination = startDestination){
        composable("signIn"){
            SignIn(navController = navController)
        }
        composable("signUpCredentials"){
            SignupCredentials(navController = navController)
        }
        composable("signUpUniversity"){
            SignupUniversity(navController = navController)
        }

        //protected screens that user has to be signed in to view
        signedInScreen(
            "home",mIsSignedIn,navController){Home(navController = navController)}
        signedInScreen(
            "allMeetups",mIsSignedIn,navController){ AllGroups(navController = navController) }
        signedInScreen(
            "createMeetup/{id}",mIsSignedIn,navController){
                backStackEntry ->
            val groupID = backStackEntry.arguments?.getString("id")
            CreateMeetup(navController = navController, groupID)
        }
        signedInScreen(
            "createPost",mIsSignedIn,navController){ CreatePost(navController = navController)}
        signedInScreen(
            "editPost/{id}",mIsSignedIn,navController
        ) { backStackEntry ->
            val postID = backStackEntry.arguments?.getString("id")
            EditPost(navController, postID)
        }
        signedInScreen(
            "meetup/{id}",mIsSignedIn,navController){
            backStackEntry ->
            val groupID = backStackEntry.arguments?.getString("id")
            SingleGroup(navController = navController, groupID)
        }
        signedInScreen(
            "userAccount",mIsSignedIn,navController){ UserAccount(navController = navController)}
        signedInScreen(
            "userPost",mIsSignedIn,navController){ UserPosts(navController = navController)}
        signedInScreen(
            "userReplies",mIsSignedIn,navController){ UserReplies(navController = navController)}
    }
}