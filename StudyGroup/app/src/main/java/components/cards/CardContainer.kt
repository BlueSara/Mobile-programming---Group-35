package components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing

@Composable
fun CardContainer(content :(@Composable (() ->Unit))? = null){
    val colors = LocalCustomColors.current
    val space = LocalSpacing.current

    Box(
        modifier = Modifier
            .border(width= 0.dp, colors.borderColor, shape = RoundedCornerShape(8.dp))
            .defaultMinSize( minHeight = space.xl)
            .fillMaxWidth()
            .shadow(
                elevation = space.xs / 2,
                shape = RoundedCornerShape(8.dp),
                clip= false,
                spotColor = colors.grey
            )
            .clip(RoundedCornerShape(8.dp))
    ){
        Column(
            modifier = Modifier
                .border(width= 2.dp, colors.borderColor, shape = RoundedCornerShape(8.dp))
                .background(colors.background)
                .defaultMinSize( minHeight = space.xl)
                .fillMaxWidth()
        ) {
            if (content != null) content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCardContainer(){
    CardContainer {  }
}