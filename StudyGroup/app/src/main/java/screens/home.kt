package screens

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.graphics.rotationMatrix
import androidx.navigation.NavHostController
import com.example.studygroup.ui.theme.LocalSpacing
import components.AppButton
import components.ButtonType
import components.PageHeadline
import components.PopUp
import components.cards.CardLarge
import handleApiReqGet
import handleApiReqPatch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import layout.Layout
import org.jetbrains.annotations.Async
import viewModel.fetchPosts
import viewModel.respondToPost
import java.util.Timer
import kotlin.concurrent.schedule

/**
 * @param navController - The navigation controller (navHostController) so which is passed to layout, layout can handle basic screen navigation.
 * @param signInToken - String, default: null. The users token used to auth user when calling api.
 * */
@Preview(showBackground = true)
@Composable
fun Home(
    navController: NavHostController ?=null,
    signInToken: String?=null
    ){

    val space = LocalSpacing.current
    val context = LocalContext.current
    //boolean for handling if post should be displayed or not.
    //for when answering a post, to have small gap between current and next post.
    var postIsChanging by remember { mutableStateOf(false) }
    var mShowErr by remember { mutableStateOf(false) }

    //holding all fetched posts, currently using mockup data
    val allPosts = remember { mutableStateListOf<Map<String, Any>>() }

    //fetching posts when screen is initially loaded
    LaunchedEffect(Unit) {
        val response = withContext(Dispatchers.IO){
            fetchPosts(context)
        }
        mShowErr = !response.ok
        if (response.ok){
            allPosts.addAll(elements = response.content as List<Map<String,Any>>)
        }
    }

    /** Updates the mutable list of posts so posts being answered are removed.
     * */
    fun updateList(){
        //setting to true so its visible to post is changing
        postIsChanging = true
        allPosts.removeAt(0)

        //fetching new posts if no posts are left in mutable list
        if (allPosts.firstOrNull() == null) {
            val response =  fetchPosts(context)
            mShowErr = !response.ok
            if (response.ok){
                allPosts.addAll(elements = response.content as List<Map<String,Any>>)
            }
        }

        //timeout to visually show that the post changes
        Timer().schedule(300){
            //returning to false so next post is displayed again
            postIsChanging = false
        }
    }

    /** Handles the users reply to a post
     * @param reply - String, default: null. The answer of the user, either "ditto", "skip" or "assist"
     * */
    fun handleReply(reply: String ?=null){
        //returning so user cannot do actions when no posts are available
        if (allPosts.firstOrNull() == null) return
        val currentPost = allPosts[0].toMap()

        CoroutineScope(Dispatchers.IO).launch {
            Log.i("CURRENT-POST", "$currentPost")
            val response = respondToPost(
                context,
                reply?: "",
                currentPost["postID"].toString()
            )

            if (!response.ok) return@launch
            withContext(Dispatchers.Main){
                updateList()
            }
        }
    }


    val sensorManager = remember {
        context.getSystemService(SENSOR_SERVICE) as SensorManager
    }

    var lastState by remember { mutableStateOf("none") }

    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {

                if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {

                    val rotation = event.values[2]

                    val newState = when {
                        rotation > 3 -> "right"
                        rotation < -3 -> "left"
                        else -> "none"
                    }

                    if (newState != lastState) {
                        when (newState) {
                            "right" -> handleReply("skip")
                            "left" -> handleReply("assist")
                        }
                    }

                    lastState = newState
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        sensorManager.registerListener(listener, rotationSensor, SensorManager.SENSOR_DELAY_GAME)

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    Layout(
        navController= navController,
        pageDetails = { PageHeadline(text="Home") },
        content = {
            //displaying only if it is not currently changing post
            if (!postIsChanging){
                //displaying the current first post in the list if available
                if (allPosts.firstOrNull() != null){
                    CardLarge(
                        post= allPosts.first()
                    )
                }
                //if no posts are available, display text
                if (allPosts.firstOrNull() == null){
                    Text(
                        text="You have answered all posts! Good job!",
                        fontSize = 18.sp)
                }
            }
            if (mShowErr){
                PopUp(
                    title = "Oh ooh..",
                    onDismiss = {mShowErr = false}
                ) {
                    Text(
                        fontSize = 20.sp,
                        text = "ah.. someone messed up, and now " +
                                "you are suffering the consequences. " +
                                "Sorry bout that.."
                    )
                }
            }
        },
        footer = {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(space.xs)
            ) {
                AppButton(
                    type = ButtonType.DANGER,
                    text = "Skip",
                    onClick = {handleReply("skip")},
                    modifier = Modifier.weight(1f,
                    )
                )
                AppButton(
                    type = ButtonType.PRIMARY,
                    text = "Ditto",
                    onClick = {handleReply("ditto")},
                    modifier = Modifier.weight(1f,
                    )
                )
                AppButton(
                    type = ButtonType.SUCCESS,
                    text = "Assist",
                    onClick = {handleReply("assist")},
                    modifier = Modifier.weight(1f,
                    )
                )
            }
        }
    )
}