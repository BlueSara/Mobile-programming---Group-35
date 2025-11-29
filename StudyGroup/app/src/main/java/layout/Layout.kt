package layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing
import domain.removeToken

/**
 * The layout component that holds the top-bar, screen content and footer.
 * It handles the main-layout responsibilities, for a consistent visual presentation.
 *  @param navController The navigation controller (navHostController) so layout can handle basic screen navigation
 *  @param arrowBack Boolean, if true, will navigate to previous screen, if false, will show chatBubble and navigate to "chats" screen
 *  @param showFilter Boolean, if true, will show option to toggle filter-pop-up
 *  @param pageDetails Composable of content to show at the top of the screen below the top-bar
 *  @param content Composable, main content to show in the screen
 *  @param footer Composable, content to insert in sticky footer container at the bottom of the screen
* */
@Composable
fun Layout(
    navController: NavHostController ?=null,
    arrowBack: Boolean = false,
    showFilter: Boolean = false,
    pageDetails: (@Composable (() -> Unit))? = null,
    content: (@Composable (() -> Unit))? = null,
    footer: (@Composable (() -> Unit))? = null,
) {

    fun navigate(screen: String = "home"){
        if (navController == null) return
        navController.navigate(screen){
            launchSingleTop = false
        }
    }

    val context = LocalContext.current

    /**For signing out user. removes stored token used with api */
    fun handleSignOut(){
        removeToken(context)
        navigate("signIn")
    }

    val colors = LocalCustomColors.current
    val space = LocalSpacing.current

    //state for handling if menu should be displayed or not
    val showMenu = rememberSaveable { mutableStateOf(value=false) }

    //main container, containing all content
    Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(colors.foreground)
        ){
            //header (top-bar) of page
            Header(
                arrowBack = arrowBack,
                onMenu = {showMenu.value = !showMenu.value},
                onClick = {
                    //if arrow back is displayed, it should navigate to the previous page
                    //if not it should navigate to the "chats" page
                    if (arrowBack) navController?.popBackStack()
                    else navController?.navigate("allMeetups")
                },
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                ){
                    if (pageDetails != null) {
                        pageDetails()
                    }

                    //container for all main-content of the page
                    Column(modifier = Modifier
                        .weight(1f)
                        .background(colors.background)
                        .padding(space.s)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                    ){
                        if (content != null) content()
                    }

                    //displaying footer and content only if footer content is provided
                    if (footer != null) Footer(footer)
                }
                //showing drop-down menu only if bool is true
                if (showMenu.value){
                    DropdownMenu(
                        onDismiss = {showMenu.value = false},
                        onNavigate = { screen -> navigate(screen)},
                        onSignout = {handleSignOut()})
                }
            }
        }
}

@Preview(showBackground = true)
@Composable
fun ShowLayout(){
    val navController = rememberNavController()
    Layout(navController = navController, showFilter = true, content = { Text("some text here") } ) {  }
}