package layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing

/**
 * The footer used in the layout component
 * @param footer Composable, default: null. Content to display in the footer (Row)
 * */
@Composable
fun Footer(footer: (@Composable (() -> Unit))? = null){
    val colors = LocalCustomColors.current
    val space = LocalSpacing.current
    Row(
        modifier = Modifier
            .background(colors.foreground)
            .padding(top = space.m, start = space.s, end = space.s, bottom = space.m)
            .fillMaxWidth()
            .shadow(elevation = space.xl, shape = RectangleShape),
        horizontalArrangement = Arrangement.spacedBy(space.xs)
    ){
        if (footer != null) footer()
    }
}

@Preview(showBackground = true)
@Composable
fun FooterPreview(){
    Footer()
}