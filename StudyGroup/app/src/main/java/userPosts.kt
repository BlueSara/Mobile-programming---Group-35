package screens.userposts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.studygroup.ui.theme.LocalSpacing
import components.PageHeadline
import components.cards.CardSmall
import layout.Layout


@Composable
fun UserPosts() {
    val navController = rememberNavController()
    val space = LocalSpacing.current
    Layout(
        navController = navController,
        arrowBack = false,
        showFilter = true,

        content = {
            PageHeadline(text = "Your posts")
            // This is your scrollable main content
            Column(
                verticalArrangement = Arrangement.spacedBy(space.s),
                modifier = Modifier.padding(space.s)
            ) {
                CardSmall()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewUserPosts() {
    UserPosts()
}
