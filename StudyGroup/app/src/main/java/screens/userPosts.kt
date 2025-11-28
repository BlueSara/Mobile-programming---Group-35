package screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.studygroup.ui.theme.LocalSpacing
import components.PageHeadline
import components.cards.CardSmall
import layout.Layout

val postsMockups = mutableListOf<Map<String, Any>>(
    mapOf(
        "title" to "POST 1, some topic about this post to get help with",
        "topic" to "topic 1",
        "subjectCode" to "prog2007",
        "subject" to "Mobile programming",
        "expirationDate" to "29-11-2025"
    ),
    mapOf(
        "title" to "POST 2, some topic about this post to get help with",
        "topic" to "topic 2",
        "subjectCode" to "prog2007",
        "subject" to "Mobile programming",
        "expirationDate" to "29-11-2025"
    ),
    mapOf(
        "title" to "POST 3, some topic about this post to get help with",
        "topic" to "topic 3",
        "subjectCode" to "prog2007",
        "subject" to "Mobile programming",
        "expirationDate" to "29-11-2025"
    )
)
@Composable
fun UserPosts(
    navController: NavHostController?= null
) {

    val space = LocalSpacing.current

    Layout(
        navController = navController,
        arrowBack = false,
        showFilter = true,
        pageDetails = { PageHeadline(text = "Your posts") },
        content = {
            // Scrollable main content
            Column(
                verticalArrangement = Arrangement.spacedBy(space.s),
            ) {
                // TODO: Have to use real data
                postsMockups.forEach { post ->
                    CardSmall(
                        post = post,
                        onClick = {
                            // TODO: Navigate to the clicked post
                            // TODO: navController?.navigate("postDetails/${post["postID"]}
                        }
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewUserPosts() {
    UserPosts()
}
