import android.content.Context
import android.util.Log
import domain.getToken
import domain.storeToken
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.emptyMap

/**Formats the api response so all content is necessary
 * @property content - Any, nullable, the json content parsed into map, or list with mapsr
 * @property code - Int, the status code returned by the api
 * @property ok - Boolean, if request was successfull or not
 * */
data class ApiResponse (
    val content: Any?,
    val code: Int?,
    val ok: Boolean,
)

fun Any.toJsonCompatible(): Any = when(this){
    is JSONObject -> keys().asSequence().associateWith { key ->
        this[key].toJsonCompatible()
    }
    is JSONArray -> List(length()) {i ->
        this[i].toJsonCompatible()
    }
    else -> this
}

/**For turning json format into relevant map
 * @return Map<String,Any>
 * */
fun JSONObject.toMap() : Map<String, Any?> = keys().asSequence().associateWith { key ->
    when (val value = this[key]){
        is JSONObject -> value.toMap()
        is JSONArray -> MutableList(value.length()){ i ->
            when (val item = value.get(i)){
                is JSONObject -> item.toMap()
                else -> item
            }
        }
        else -> value
    }
}

/** For formatting the response into proper map
 * @param connection - HttpURLConnection, no default. The connection to the api
 * @return ApiResponse - The formatted response format
 * */
fun formatResponse(connection: HttpURLConnection): ApiResponse {
    val code = connection.responseCode
    val ok = connection.responseCode in 200..299
    val unparsedContent = connection.inputStream.bufferedReader().readText()

    var jsonContent: Any = emptyMap<String, Any>()

    if (code != 204){
        jsonContent = try {
            JSONObject(unparsedContent).toJsonCompatible()
        } catch(e : Exception){
            JSONArray(unparsedContent).toJsonCompatible()
        }
    }

    return ApiResponse(jsonContent, code, ok)
}

/**Formats the request for it to have correct method, include headers and request property
 * @param connection - HttpURLConnection, no default. The url connection.
 * @param method - String, no default. The method, e.g. "POST", "GET" etc being made
 * @param token - String, default: null. auth token used with api if authorized endpoint
 * */
fun formatRequest(connection: HttpURLConnection, method: String, token: String ?= null){
    if (token != null) connection.setRequestProperty("Authorization", "Bearer $token")

    connection.requestMethod = method
    connection.connectTimeout = 60000
    connection.readTimeout = 60000
    connection.setRequestProperty("Accept", "application/json")
}

/**This is used for making GET-requests to the api
 * @param path - String, default: null. The route to contact in the api. Should have a "/" at the beginning, e.g. formatted as "/the/path"
 * @param context - Context, no default. The LocalContext.current for getting the api_token
 * @return ApiResponse - data class. Returns the db response, can be null
 * */
fun handleApiReqGet(path: String, context: Context): ApiResponse{
    val url = "https://studygroup-api.onrender.com$path"
    val connection = URL(url).openConnection() as HttpURLConnection

    formatRequest(connection, "GET", getToken(context))

    return try{
        val response = formatResponse(connection)
        connection.disconnect()

        response

    }catch(e : Exception){
        connection.disconnect()
        Log.e("API-REQUEST-LOG-GET", "Error: ${e.toString()}", e)
        ApiResponse(null, null, false)
    }
}

/**This is used for making POST-requests to the api
 * @param path - String, no default. The route to contact in the api. Should have a "/" at the beginning, e.g. formatted as  "/the/path"
 * @param body - Map<String,Any>, default: emptyMap(). The content sent to the api.
 * @param context - Context, no default. The LocalContext.current for setting and fetching api_token
 * @return ApiResponse - data class. Returns the db response, can be null
 * */
fun handleApiReqPost(path: String, body: Map<String, Any> ?= emptyMap(), context: Context): ApiResponse{
    val url = "https://studygroup-api.onrender.com$path"
    val bodyContent = JSONObject(body as Map<*, *>).toString()
    val connection = URL(url).openConnection() as HttpURLConnection

    //checks if it is an auth route or not. Auth-routes should not include a auth-token
    val isSignInRoute = path == "/auth/signin"
    val token = if (isSignInRoute) getToken(context) else null

    formatRequest(connection, "POST", token)
    connection.doOutput = true

    return try{
        connection.outputStream.use { it.write(bodyContent.toByteArray()) }

        val response = formatResponse(connection)
        connection.disconnect()

        if (isSignInRoute){
            val token = (response.content as? Map<*,*>)?.get("token") as? String ?: ""
            storeToken(context = context, token)
        }

        response
    } catch (e : Exception){
        connection.disconnect()
        Log.e("API-REQUEST-LOG-POST", "Error: ${e.toString()}", e)
        ApiResponse(null, null, false)
    }
}

/**This is used for making PATCH-requests to the api
 * @param path - String, no default. The route to contact in the api. Should have a "/" at the beginning, e.g. formatted as  "/the/path"
 * @param body - Map<String,Any>, default: emptyMap(). The content sent to the api.
 * @param context - Context, no default. The LocalContext.current for setting and fetching api_token
 * @return ApiResponse - data class. Returns the db response, can be null
 * */
fun handleApiReqPatch(path: String, context: Context, body: Map<String, Any> ?= null): ApiResponse{
    val url = "https://studygroup-api.onrender.com$path"
    val bodyContent = JSONObject(body as Map<*, *>).toString()
    val connection = URL(url).openConnection() as HttpURLConnection

    formatRequest(connection, "PATCH", getToken(context))
    connection.doOutput = true

    return try{
        connection.outputStream.use { it.write(bodyContent.toByteArray()) }

        val response = formatResponse(connection)
        connection.disconnect()

        response
    } catch (e : Exception){
        connection.disconnect()
        Log.e("API-REQUEST-LOG-POST", "Error: ${e.toString()}", e)
        ApiResponse(null, null, false)
    }
}

/**This is used for making PUT-requests to the api
 * @param path - String, no default. The route to contact in the api. Should have a "/" at the beginning, e.g. formatted as  "/the/path"
 * @param body - Map<String,Any>, default: emptyMap(). The content sent to the api.
 * @param context - Context, no default. The LocalContext.current for setting and fetching api_token
 * @return ApiResponse - data class. Returns the db response, can be null
 * */
fun handleApiReqPut(path: String, context: Context, body: Map<String, Any> ?= null): ApiResponse{
    val url = "https://studygroup-api.onrender.com$path"
    val bodyContent = JSONObject(body as Map<*, *>).toString()
    val connection = URL(url).openConnection() as HttpURLConnection

    formatRequest(connection, "PUT", getToken(context))
    connection.doOutput = true

    return try{
        connection.outputStream.use { it.write(bodyContent.toByteArray()) }

        val response = formatResponse(connection)
        connection.disconnect()

        response
    } catch (e : Exception){
        connection.disconnect()
        Log.e("API-REQUEST-LOG-POST", "Error: ${e.toString()}", e)
        ApiResponse(null, null, false)
    }
}


/**This is used for making DELETE-requests to the api
 * @param path - String, no default. The route to contact in the api. Should have a "/" at the beginning, e.g. formatted as  "/the/path"
 * @param context - Context, no default. The LocalContext.current for setting and fetching api_token
 * @return ApiResponse - data class. Returns the db response, can be null
 * */
fun handleApiReqDelete(path: String, context: Context): ApiResponse{
    val url = "https://studygroup-api.onrender.com$path"
    val connection = URL(url).openConnection() as HttpURLConnection

    formatRequest(connection, "DELETE", getToken(context))

    return try{
        val response = formatResponse(connection)
        connection.disconnect()

        response
    } catch (e : Exception){
        connection.disconnect()
        Log.e("API-REQUEST-LOG-POST", "Error: ${e.toString()}", e)
        ApiResponse(null, null, false)
    }
}