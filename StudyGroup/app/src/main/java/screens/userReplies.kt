package screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import components.PageHeadline
import components.cards.CardSmall
import components.icons.EditIcon
import layout.Layout
import com.example.studygroup.ui.theme.LocalSpacing
import components.AppButton
import components.ButtonType
import components.PopUp

val mockdata: MutableList<MutableMap<String, Any>> = mutableListOf(
    mutableMapOf(
        "title" to "tittel 1",
        "postID" to "A7tTGFUUO1JCqqIw1Tphfdf",
        "subject" to  "avansert",
        "subjectCode" to "PROG2006",
        "expirationDate" to "",
        "topic" to "need help pls",
        "response" to "skip"
    ),
    mutableMapOf(
        "title" to "asfdasdfasdfasdf",
        "postID" to "KUB7PYq8QAVum7LP2G2fgtha",
        "subject" to  "objektorientert programmering",
        "subjectCode" to "PROG1003",
        "expirationDate" to "",
        "topic" to "need help pls",
        "response" to "skip"
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
        "response" to "assist"
    ),
    mutableMapOf(
        "title" to "tittel 2",
        "postID" to "KUB7PYq8QAVum7LP2G2fgj533",
        "subject" to  "objektorientert programmering",
        "subjectCode" to "PROG1003",
        "expirationDate" to "",
        "topic" to "need help pls",
        "response" to "skip"
    ),
)
//used to iterate over to create AppButtons instead of having to manually create each button
val buttonOptions = listOf(
    listOf("Skip", ButtonType.DANGER),
    listOf("Ditto", ButtonType.PRIMARY),
    listOf("Assist", ButtonType.SUCCESS),
)

/**
 * Screen that lets the user see their responses.
 * They can update their status on each post by clicking the pen icon.
 */
@Composable
@Preview(showBackground = true)
fun UserReplies(
    navController: NavHostController ?=null,){
    val mSelectedPost = remember { mutableStateMapOf<String, Any>() }
    var mAllPosts = remember { mutableListOf<Map<String, Any>>().toMutableStateList() }
    val space = LocalSpacing.current

    /**Function calling api-handler to get posts*/
    fun getPosts(){
        // TODO : Get actual post data via function calling api instead of mockup
        mAllPosts = mockdata.toMutableStateList()
    }

    /** Function calls the api, updates the response and UI
     * @param response - String. No default value. The response that should be updated to
     * */
    fun handleUpdateResponse(response: String){
        // TODO : add actual function-call to api-handler function and update response
        val updateSuccess = true
        val i = mAllPosts.indexOfFirst {it["postID"] == mSelectedPost["postID"]}
        if (response == mSelectedPost["response"] || !updateSuccess || i == -1)  return

        //updating the response in mSelectedPost, and updating the item in list (UI)
        mSelectedPost["response"] = response.lowercase()
        mAllPosts[i] = mSelectedPost.toMutableMap()
    }
    getPosts()

    Layout(
        navController= navController,
        arrowBack = false,
        pageDetails = { PageHeadline("Your Responses") },
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(space.m),
            ) {
                mAllPosts.forEach {post ->
                    CardSmall (
                        post = post,
                        onClick = {mSelectedPost.putAll(post) },
                        showResponse = true,
                        icon = { EditIcon() }
                    )
                }
            }
            if (!mSelectedPost.isEmpty()){
                PopUp (
                    onDismiss = { mSelectedPost.clear() },
                    title = "Change your reply",
                    content = {
                        Row( horizontalArrangement = Arrangement.spacedBy(space.m) ) {
                            buttonOptions.forEach { btn ->
                                AppButton(
                                    modifier = Modifier.weight(1f),
                                    type = btn[1] as ButtonType,
                                    text = btn[0].toString(),
                                    onClick = {
                                        handleUpdateResponse(response = btn[0].toString().lowercase())
                                        mSelectedPost.clear()
                                    }
                                )
                            }
                        }
                    }
                )
            }
        }
    )
}