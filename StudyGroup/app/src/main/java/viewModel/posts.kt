package viewModel

import ApiResponse
import handleApiReqGet
import android.content.Context
import android.util.Log
import handleApiReqDelete
import handleApiReqPatch
import handleApiReqPut
import java.time.LocalDate

/** Used to call api in order to fetch posts to display in home-screen.
 * @param context - Context, no default. LocalContext.current used when fetching api
 * @return ApiResponse - response from api
 * */
fun fetchPosts(context: Context): ApiResponse {
    val response = handleApiReqGet("/posts", context)
    if (response.content == null){
        val content = listOf<Map<String, Any>>()
        return ApiResponse(content, response.code, response.ok)
    }
    return response
}

/** Used to call api in order to answer a post.
 * @param context - Context, no default. LocalContext.current used when fetching api
 * @param response - String?, no default. The response user gave to a post.
 * @param postID - String?, no default. The id of the post being responded to
 * @return ApiResponse - response from api
 * */
fun respondToPost(context: Context, response: String?, postID: String?) : ApiResponse {
    val body = mapOf("answer" to response)

    if (postID.isNullOrEmpty()) return ApiResponse(null, null, false)
    if (response.isNullOrEmpty()) return ApiResponse(null, null, false)

    val apiRes = handleApiReqPatch(
        path = "/post/${postID}/answer",
        context = context,
        body = body as Map<String, Any>?
    )

    Log.i("CURRENT-REPLY", "$apiRes")
    return apiRes
}

/**Handler for deleting posts
 * @param postID - String, no defaults. The id of the post to delete
 * @param context - Context, the context used by api-handler.
 * @return - Boolean, returns boolean if api call was success or not
 * */
fun handleDeletePost(postID: String?, context: Context): Boolean {
    if (postID == null) return false
    val response = handleApiReqDelete("/post/$postID", context)
    return response.ok
}

/**Handler for updating a post
 * @param postID - String?, no default. The id of the post to update
 * @param exp - LocalDate, no default. The expiration date for the post
 * @param topic - String, no default. The topic of the post
 * @param subject - String, no default. The id of the selected subject.
 * @param description - String, no default. The description (title) for the post.
 * @param useLocation - Boolean, no default. Boolean location should be used or not.
 * @param context - Context, no default. The context used by the api handler
 * @param xCoord - Double, default: null. The x coordinates to use if requested
 * @param yCoord - Double, default: null. The y coordinates to use if requested
 * @return Boolean, if the request was successfull or not
 * */
fun handleUpdatePost(
    postID: String?,
    exp: LocalDate,
    topic: String,
    subject: String,
    description: String,
    useLocation: Boolean,
    context: Context,
    xCoord: Double?=null,
    yCoord: Double?=null
): Boolean {
    if (postID == null) return false
    val formattedDate = "${exp}T00:00:00Z"

    val body: Map<String, Any> = mapOf(
        "title" to topic,
        "subjectID" to subject,
        "topic" to description,
        "useProximity" to useLocation,
        "expirationDate" to formattedDate,
        "xCoord" to xCoord,
        "yCoord" to yCoord
    ) as Map<String, Any>
    val res = handleApiReqPut("/posts/$postID", context, body)
    return res.ok
}