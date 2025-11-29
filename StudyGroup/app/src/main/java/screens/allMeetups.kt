package screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing
import components.PageHeadline
import components.cards.CardSmall
import components.icons.ArrowLeft
import components.icons.ArrowRight
import components.icons.EditIcon
import layout.Layout
import utils.ConnectivityStatus


@Composable
@Preview(showBackground = true)
fun AllMeetups(
    navController: NavHostController ?=null,){

    val space = LocalSpacing.current

    // NOTE: all future mock data should reside in the beginning of the screen function
    // otherwise we will get naming conflicts
    val mockdata: MutableList<MutableMap<String, Any>> = mutableListOf(
        mutableMapOf(
            "title" to "tittel 1",
            "postID" to "A7tTGFUUO1JCqqIw1Tphfdf",
            "subject" to  "avansert",
            "subjectCode" to "PROG2006",
            "expirationDate" to "",
            "topic" to "need help pls",
            "response" to "ditto"
        ),
        mutableMapOf(
            "title" to "asfdasdfasdfasdf",
            "postID" to "KUB7PYq8QAVum7LP2G2fgtha",
            "subject" to  "objektorientert programmering",
            "subjectCode" to "PROG1003",
            "expirationDate" to "",
            "topic" to "need help pls",
            "response" to "assist"
        ),
        mutableMapOf(
            "title" to "trenger hjelp med mobil asap zulu plis :((",
            "postID" to "KUB7PYq8QAVum7LP2G2fhsac",
            "subject" to  "objektorientert programmering",
            "subjectCode" to "PROG2007",
            "expirationDate" to "",
            "topic" to "need help pls",
            "response" to "ditto"
        ),
        mutableMapOf(
            "title" to "tittel 2",
            "postID" to "KUB7PYq8QAVum7LP2G2fgdjya",
            "subject" to  "objektorientert programmering",
            "subjectCode" to "PROG1003",
            "expirationDate" to "",
            "topic" to "need help pls",
            "response" to "skip"
        ),
        mutableMapOf(
            "title" to "tittel 2",
            "postID" to "KUB7PYq8QAVum7LP2G2fgj533",
            "subject" to  "objektorientert programmering",
            "subjectCode" to "PROG1003",
            "expirationDate" to "",
            "topic" to "need help pls",
            "response" to "assist"
        ),
    )

    // only keep "ditto" or "assist" posts
    val noSkipPosts = mockdata.filter { it["response"] == "ditto" || it["response"] == "assist"}



    Layout(
        navController= navController,
        arrowBack = true,
        pageDetails = { PageHeadline("Schedules overview") },
        content = {
            ConnectivityStatus()
            Column(
                verticalArrangement = Arrangement.spacedBy(space.m),
            ) {

                // generates a card for each filter
                if (!noSkipPosts.isEmpty()) {
                    noSkipPosts.forEach { post ->
                        CardSmall(
                            post = post,
                            onClick = {
                                // TODO: navigate to screen
                            },
                            showResponse = true,
                            icon = { ArrowRight() }
                        )
                    }
                } else {
                    Text(text = "No posts to show.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth())
                }
            }
        }
    )
}