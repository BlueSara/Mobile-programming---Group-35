package screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing
import components.PageHeadline
import components.cards.CardSmall
import components.icons.ArrowRight
import handleApiReqGet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import layout.Layout
import utils.ConnectivityStatus


@Composable
@Preview(showBackground = true)
fun AllMeetups(
    navController: NavHostController ?=null,){

    val space = LocalSpacing.current
    val context = LocalContext.current
    val mGroups = remember { mutableStateListOf<Map<String, Any>>() }

    /**Fetches the data (groups) for this screen
     * */
    fun fetchData(){
        CoroutineScope(Dispatchers.IO).launch{
            val groupsResponse = handleApiReqGet("/post/groups", context)
            if (!groupsResponse.ok) {
                //TODO : HANDLE TO SHOW ERR IF ERR WAS RETURNED
                return@launch
            }
            mGroups.addAll(elements = groupsResponse.content as List<Map<String,Any>>)
        }
    }

    fetchData()

    Layout(
        navController= navController,
        arrowBack = true,
        pageDetails = { PageHeadline("Schedules overview") },
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(space.m),
            ) {
                mGroups.forEach { group ->
                    CardSmall(
                        post = group,
                        onClick = {
                            navController?.navigate("meetup/${group["groupID"]}")
                        },
                        showResponse = true,
                        icon = { ArrowRight() }
                    )
                }

                if (mGroups.isEmpty()){
                    Text(text = "No posts to show.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth())
                }
            }
        }
    )
}