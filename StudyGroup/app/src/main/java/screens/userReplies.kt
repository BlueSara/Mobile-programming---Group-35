package screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import components.PageHeadline
import components.cards.CardSmall
import components.icons.ChatBubble
import components.icons.EditIcon
import layout.Layout
import kotlin.collections.set
import androidx.compose.runtime.toMutableStateMap
import components.AppButton
import components.ButtonType
import components.PopUp

val mockdata: MutableList<MutableMap<String, Any>> = mutableListOf(
    mutableMapOf(
        "title" to "tittel 1",
        "postID" to "A7tTGFUUO1JCqqIw1Tph",
        "subject" to  "avansert",
        "subjectCode" to "PROG2006",
        "expirationDate" to "",
        "topic" to "need help pls",
        "response" to "skip"
    ),
    mutableMapOf(
        "title" to "asfdasdfasdfasdf",
        "postID" to "KUB7PYq8QAVum7LP2G2f",
        "subject" to  "objektorientert programmering",
        "subjectCode" to "PROG1003",
        "expirationDate" to "",
        "topic" to "need help pls",
        "response" to "skip"
    ),
    mutableMapOf(
        "title" to "trenger hjelp med mobil asap zulu plis :((",
        "postID" to "KUB7PYq8QAVum7LP2G2f",
        "subject" to  "objektorientert programmering",
        "subjectCode" to "PROG2007",
        "expirationDate" to "",
        "topic" to "need help pls",
        "response" to "ditto"
    ),
    mutableMapOf(
        "title" to "tittel 2",
        "postID" to "KUB7PYq8QAVum7LP2G2f",
        "subject" to  "objektorientert programmering",
        "subjectCode" to "PROG1003",
        "expirationDate" to "",
        "topic" to "need help pls",
        "response" to "assist"
    ),
    mutableMapOf(
        "title" to "tittel 2",
        "postID" to "KUB7PYq8QAVum7LP2G2f",
        "subject" to  "objektorientert programmering",
        "subjectCode" to "PROG1003",
        "expirationDate" to "",
        "topic" to "need help pls",
        "response" to "skip"
    ),
)

/**
 * Screen that lets the user see their responses.
 * They can update their status on each post by clicking the pen icon.
 */
@Composable
@Preview(showBackground = true)
fun UserReplies(
    navController: NavHostController ?=null,){

    // TODO: actually get some data here from the API instead of mock data


    Layout(
        navController= navController,
        arrowBack = false,
        pageDetails = { PageHeadline("Your Responses") },
        content = {

            // TODO: replace mock data with actual data
            mockdata.forEach {post ->
                NewResponse(post)
            }



        }

    )
}

/**
 * Creates a single new response to display to the user.
 * This is its own function because each response needs to store and update some state about itself,
 * which is very unpractical to do without a separate function.
 *
 * @param post the data to display
 */
@Composable
fun NewResponse(
    post: MutableMap<String, Any>
) {
    // creates a mutable state from the post data
    val mPost = remember { mutableStateMapOf<String, Any>().apply { putAll(post) } }
    val mShowPopup = remember { mutableStateOf(false) }

    CardSmall (
        post = mPost,
        onClick = {
            mShowPopup.value = true
        },
        showResponse = true,
        icon = { EditIcon() }
    )

    if (mShowPopup.value) {
        PopUp (
            onDismiss = { mShowPopup.value = false },
            title = "Change your reply",
            content = {
                Row {
                    AppButton(
                        modifier = Modifier.weight(1f),
                        type = ButtonType.DANGER,
                        onClick = {

                            // update value if we managed to successfully managed to upload it to the database
                            val oldResponse = mPost["response"] as Any
                            mPost["response"] = "skip"
                            if (!tryUploadResponseToDatabase(mPost)) mPost["response"] = oldResponse
                            mShowPopup.value = false },
                        text = "Skip"
                    )


                    Spacer(Modifier.width(8.dp))


                    AppButton(
                        modifier = Modifier.weight(1f),
                        type = ButtonType.PRIMARY,
                        onClick = {
                            val oldResponse = mPost["response"] as Any
                            mPost["response"] = "ditto"
                            if (!tryUploadResponseToDatabase(mPost)) mPost["response"] = oldResponse
                            mShowPopup.value = false },
                        text = "Ditto"
                    )


                    Spacer(Modifier.width(8.dp))


                    AppButton(
                        modifier = Modifier.weight(1f),
                        type = ButtonType.SUCCESS,
                        onClick = {
                            val oldResponse = mPost["response"] as Any
                            mPost["response"] = "assist"
                            if (!tryUploadResponseToDatabase(mPost)) mPost["response"] = oldResponse
                            mShowPopup.value = false
                                  },
                        text = "Assist"
                    )
                }
            }
        )
    }

    Spacer(Modifier.height(8.dp))
}


// try to upload the new response to the database
fun tryUploadResponseToDatabase (
    post: Map<String, Any>
): Boolean {

    // TODO: implement

    // val result = tryUpdateDatabase(post)
    //
    // if (result != good) {
    //      return false
    // }

    return true
}