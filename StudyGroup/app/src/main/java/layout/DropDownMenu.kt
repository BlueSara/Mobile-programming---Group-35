package layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygroup.R
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing
import components.AppButton
import components.ButtonType


//the drop-down menu displayed when menu-button is clicked
/**
 * This is the "hamburger-menu" used in the layout to show pages that can be navigated to
 * @param onDismiss Unit, default: null. Callback for when clicking outside the menu in order to close it
 * @param onNavigate Unit, default: null. Callback for clicking a screen option to navigate to. Returns a string
 * */
@Composable
fun DropdownMenu(
    onDismiss: (() -> Unit)? = null,
    onNavigate: ((String) -> Unit)? = null,
    onSignout: (() -> Unit)? = null,
){
    val colors = LocalCustomColors.current
    val space = LocalSpacing.current

    val screens = listOf(
        Pair("Home", "home"),
        Pair("Create new post", "createPost"),
        Pair("View your posts", "userPost"),
        Pair("View your replies", "userReplies"),
        Pair("View your profile", "userAccount"),

    )

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
                .padding(bottom = space.s, start = space.s, end = space.s, top = space.xl),
            verticalArrangement = Arrangement.spacedBy(space.l)
        ){
            screens.forEach { screen  ->
                Button(
                    onClick = { onNavigate?.invoke(screen.second) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.background,
                        contentColor = colors.black,
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RectangleShape,
                    contentPadding = PaddingValues(vertical = 0.dp),
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 0.dp, top= space.s)
                        ,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = space.m)
                            ,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(text=screen.first, fontSize = 20.sp, fontWeight = FontWeight.Light, textAlign = TextAlign.Start)
                            Icon(
                                painter = painterResource(id = R.drawable.greyarrowleft),
                                contentDescription = "Go to page",
                                tint = colors.borderColor,
                            )
                        }
                        Box(modifier = Modifier
                            .background(colors.borderColor)
                            .fillMaxWidth()
                            .height(1.dp)
                        )
                    }
                }
            }
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                type = ButtonType.DANGER,
                onClick = {onSignout?.invoke()},
                text = "Sign out"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDropDown(){
    DropdownMenu()
}
