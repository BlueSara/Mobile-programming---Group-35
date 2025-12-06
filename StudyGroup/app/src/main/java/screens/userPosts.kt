package screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.studygroup.ui.theme.LocalSpacing
import components.PageHeadline
import components.TextInput
import components.cards.CardSmall
import components.icons.EditIcon
import handleApiReqGet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import layout.Layout

@Composable
fun UserPosts(
    navController: NavHostController?= null
) {

    val space = LocalSpacing.current
    val context = LocalContext.current

    // Holds all posts created by the user
    val mUserPosts = remember { mutableStateListOf<Map<String,Any>>() }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val res = handleApiReqGet("/posts/own", context)

            if(res.ok) {

                val raw = res.content as? List<Map<String, Any>> ?: emptyList()

                val list = raw.map { post ->
                    mapOf(
                        "postID" to post["PostID"],
                        "title" to post["Title"],
                        "topic" to post["Topic"],
                        "subject" to post["Subject"],
                        "subjectCode" to post["SubjectCode"],
                        "expirationDate" to post["ExpirationDate"]
                    )
                }
                mUserPosts.clear()
                mUserPosts.addAll(list as Collection<Map<String, Any>>)

                Log.e("USER_POSTS", "load: ${res.code} | ${res.content}")
            } else {
                Log.e("USER_POSTS", "failed load: ${res.code} | ${res.content}")
            }
        }
    }
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
                if (mUserPosts.isEmpty())
                    TextInput(
                        value = "You have no posts yet!"
                    )
                mUserPosts.forEach { post ->
                    CardSmall(
                        post = post,
                        onClick = {
                            val id = post["postID"].toString()
                            navController?.navigate("editPost/$id")

                        },
                        icon = { EditIcon() }
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
