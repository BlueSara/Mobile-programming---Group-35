package components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing

/**
 * PopUp
 *
 * A scrollable popup overlaying other screen content.
 * Has a transparent black background around the popup. This allows
 * the content underneath to remain visible while popup is active.
 *
 * @param onDismiss Unit, default: null. callback triggered when the popup should be closed
 * @param title String, default: "". title text displayed in the popup header
 * @param content @Composable, default: null. content displayed inside the popup
 *
 */
@Composable
fun PopUp(
    onDismiss: (() -> Unit)? = null,
    title: String = "",
    content: (@Composable () -> Unit )?= null
) {
    val colors = LocalCustomColors.current
    val space = LocalSpacing.current
    val shape = RoundedCornerShape(12.dp)

    Dialog(
        onDismissRequest = { onDismiss?.invoke() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets(0))
                .clickable { onDismiss?.invoke() },
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .padding(horizontal = space.xs)
                    .clip(shape)
                    .background(colors.background)
                    .clickable(enabled = false) {},
                verticalArrangement = Arrangement.Top
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.foreground)
                        .padding(vertical = 0.dp, horizontal = 0.dp),

                    contentAlignment = Alignment.Center
                ) {

                    IconButton(
                        onClick = { onDismiss?.invoke() },
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 5.dp)
                        ,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back",
                            modifier = Modifier
                                .align(Alignment.CenterStart),
                        )
                    }

                    Text(
                        text = title,
                        fontSize = 22.sp,
                        style = MaterialTheme.typography.titleMedium,

                        )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(space.s, space.m, space.s, space.s)
                        .verticalScroll(state = rememberScrollState()),
                    verticalArrangement = Arrangement.Top
                ) {
                    if (content != null) content()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewPopup() {
    var showthing by remember { mutableStateOf(false) }

    Button(onClick = {showthing = !showthing}) {
        Text(text = "<text>")
    }

        if (showthing)Box(Modifier
            .fillMaxSize()
        ) {
            PopUp(
            title = "<this is a very long title>",
            onDismiss = {showthing = false}
        ) {
            Text("Body content here", fontSize = 50.sp)
            Text("Body content here", fontSize = 50.sp)
            Text("Body content here", fontSize = 50.sp)
            Text("Body content here", fontSize = 50.sp)
            Text("Body content here", fontSize = 50.sp)
            Text("Body content here", fontSize = 50.sp)
            Text("Body content here", fontSize = 50.sp)

        }
    }
}
