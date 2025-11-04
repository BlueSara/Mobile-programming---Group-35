package components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing

@Composable
fun PageHeadline(
    text: String = "",
) {
    val colors = LocalCustomColors.current
    val space = LocalSpacing.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = colors.borderColor,
                shape = RectangleShape
            )
            .padding(top = space.l, bottom = space.m, )
            .then(
                Modifier.drawBehind {
                    val strokeWidth = 1.dp.toPx()

                    drawLine(
                        color = colors.borderColor,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                }
            )
            .padding(horizontal = space.s, vertical = 0.dp)
    ) {
        Text(
            text = text,
            fontSize = 26.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(bottom = space.xs)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPageHeadLine(){
    PageHeadline(text = "hhahahahah")
}