package layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing
import components.icons.ArrowLeft
import components.icons.ChatBubble

/**
 * @param arrowBack Boolean, default: false. Handles if header show a arrow back (true), or chat bubble icon(false)
 * @param onMenu Unit, default: null. Callback function for when menu has been clicked
 * @param onClick Unit, default: null. Callback function for when icon (top left) has been clicked.
 * */
@Composable
fun Header(arrowBack: Boolean = false,
           onMenu: (() -> Unit)? = null,
           onClick: (() -> Unit)? = null,
){
    val colors = LocalCustomColors.current
    val space = LocalSpacing.current

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
                onClick = {onClick?.invoke()},
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.foreground,
                    contentColor = colors.black,
                ),
                contentPadding = PaddingValues(vertical = space.xs),
                shape = RoundedCornerShape(space.s),
                modifier = Modifier
                    .width(space.xl)
            ){
                if (arrowBack) ArrowLeft()
                else ChatBubble()
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

            //menu-button
            Button(
                onClick = {onMenu?.invoke()},
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
}

@Preview(showBackground = true)
@Composable
fun PreviewHeader(){
    Header(arrowBack = false)
    Header(arrowBack = true)
}