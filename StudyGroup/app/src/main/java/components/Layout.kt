package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing

//the drop-down menu displayed when menu-button is clicked
@Composable
fun DropdownMenu(
    onDismiss: (() -> Unit)? = null,
    onNavigate: ((String) -> Unit)? = null
){
    val colors = LocalCustomColors.current
    val space = LocalSpacing.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ){
        Button(
            onClick = {onDismiss?.invoke()},
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.black,
                contentColor = colors.black,
            ),
            modifier = Modifier
                .fillMaxHeight()
                .alpha(0.5f)
                .fillMaxWidth(),
            shape = RectangleShape

        ){}
        Column(
            modifier = Modifier
                .background(colors.background)
                .fillMaxWidth()
                .padding(space.s)
        ){
            Button(
                onClick = { onNavigate?.invoke("home") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.background,
                    contentColor = colors.black,
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(space.s)
            ){
                Text(text="Home")
            }
            Button(
                onClick = { onNavigate?.invoke("other") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.background,
                    contentColor = colors.black,
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(space.s)
            ){
                Text(text="other")
            }
        }
    }
}

@Composable
fun Layout(
    navController: NavHostController ?=null,
    arrowBack: Boolean = false,
    showFilter: Boolean = false,
    onFilter: (() -> Unit)? = null,
    pageInfo: (@Composable (() -> Unit))? = null,
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
    Box(modifier = Modifier.fillMaxSize()
    ){
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(colors.foreground)
        ){
            //header (top-bar) of page
            Row(modifier = Modifier
                .statusBarsPadding()
                .background(colors.foreground)
                .fillMaxWidth()
                .height(space.xl)
                .padding(horizontal = space.s),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(space.s)
                ){
                    //button in top left corner
                    Button(
                        onClick = {
                            //if arrow back is displayed, it should navigate to the previous page
                            //if not it should navigate to the "chats" page
                            if (arrowBack) navController?.popBackStack()
                            else navController?.navigate("home")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.foreground,
                            contentColor = colors.black,
                        ),
                        contentPadding = PaddingValues(vertical = space.xs),
                        shape = RoundedCornerShape(space.s),
                        modifier = Modifier
                            .width(space.xl)
                    ){
                        //render icon in btn only if not null
                        if (arrowBack) {
                            //placeholder for arrow back icon
                            Text(text = "A")
                        }
                        else{
                            //placeholder for chat bubble icon
                            Text(text = "C")
                        }
                    }
                    Text(
                        text="StudyGroup",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }
                //For callback to show filter-menu
                Row(
                    horizontalArrangement = Arrangement.spacedBy(space.s)
                ){
                    //only show filter-button if bool is true
                    if (showFilter){
                        Button(
                            onClick = {onFilter?.invoke()},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.foreground,
                                contentColor = colors.black,
                            ),
                            contentPadding = PaddingValues(vertical = space.xs),
                            shape = RoundedCornerShape(0.dp),
                            modifier = Modifier
                                .width(space.xl)
                        ){

                        }
                    }

                    //menu-button
                    Button(
                        onClick = {showMenu.value = !showMenu.value},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.foreground,
                            contentColor = colors.black,
                        ),
                        contentPadding = PaddingValues(vertical = 10.dp),
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier
                            .width(space.xl)
                    ){
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .width(space.l)
                                .height(space.l)
                        ){
                            repeat(3){
                                Box(modifier = Modifier
                                    .background(colors.black)
                                    .fillMaxWidth()
                                    .height(3.dp)
                                )
                            }
                        }
                    }
                }

            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                ){
                    if (pageInfo != null) {
                        pageInfo()
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
                    if (footer != null){
                        Row(){
                            footer()
                        }
                    }
                }
                //showing drop-down menu only if bool is true
                if (showMenu.value){
                    DropdownMenu(
                        onDismiss = {showMenu.value = false},
                        onNavigate = {screen -> navigate(screen)})
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowLayout(){
    val navController = rememberNavController()
    Layout(navController = navController, showFilter = true) {  }
}