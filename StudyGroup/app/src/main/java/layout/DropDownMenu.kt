package layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing


//the drop-down menu displayed when menu-button is clicked
/**
 * This is the "hamburger-menu" used in the layout to show pages that can be navigated to
 * @param onDismiss Unit, default: null. Callback for when clicking outside the menu in order to close it
 * @param onNavigate Unit, default: null. Callback for clicking a screen option to navigate to. Returns a string
 * */
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