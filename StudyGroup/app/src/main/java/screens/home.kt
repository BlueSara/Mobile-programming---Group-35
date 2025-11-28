package screens

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
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
import components.cards.CardLarge
import layout.Layout
import java.util.Timer
import kotlin.concurrent.schedule

//just mockup data for temp posts
val postsMockup = mutableListOf<Map<String, Any>>(
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
    //boolean for handling if post should be displayed or not.
    //for when answering a post, to have small gap between current and next post.
    var postIsChanging by remember { mutableStateOf(false) }

    //holding all fetched posts, currently using mockup data
    var allPosts = remember { postsMockup.toMutableStateList() }

    /** Used to call api in order to fetch more posts.
     * Needs to use signInToken when calling api.
     * */
    fun fetchPosts(){
        //currently set to just use mockup data
        allPosts = postsMockup.toMutableStateList()
        //insert proper logic to fetch posts here
    }

    /** Updates the mutable list of posts so posts being answered are removed.
     * */
    fun updateList(){
        //setting to true so its visible to post is changing
        postIsChanging = true
        allPosts.removeAt(0)

        //fetching new posts if no posts are left in mutable list
        if (allPosts.firstOrNull() == null) fetchPosts()
        //timeout to visually show that the post changes
        Timer().schedule(500){
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
        updateList()
        //insert logic for calling api to answer post here
    }


    val context = LocalContext.current
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