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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing

@Composable
fun Layout(
    navController: NavHostController ?=null,
    arrowBack: Boolean = false,
    showFilter: Boolean = false,
    onFilter: (() -> Unit)? = null,
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
                    else navController?.navigate("home")
                },
                onFilter ={onFilter?.invoke()},
                showFilter = showFilter
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
                        onNavigate = { screen -> navigate(screen)})
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