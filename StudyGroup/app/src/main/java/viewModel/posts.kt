package viewModel

import ApiResponse
import handleApiReqGet
import android.content.Context
import android.util.Log
import handleApiReqPatch

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