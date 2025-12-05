package screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.studygroup.ui.theme.LocalSpacing
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import components.AppButton
import components.ButtonType
import components.DateInput
import components.PageHeadline
import components.Select
import components.TextArea
import components.TextInput
import dataLayer.firebase.UniversityService
import handleApiReqGet
import handleApiReqPost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import layout.Layout
import java.time.LocalDate
import dataLayer.firebase.Subject



val mockUpSubjects:List<Map<String,String>> =  listOf(
    mapOf(
        Pair("id", "1234"),
        Pair("more_data", "what"),
        Pair("name", "NTNU")
    ),
    mapOf(
        Pair("name", "Oslo Met"),
        Pair("id", "67")
    ),
    mapOf(
        Pair("name", "Høyskolen Kristiania"),
        Pair("id", "9876"),
        Pair("more_data", "omg"),
        Pair("even_more_data", "no way")
    ),
)
// API_DEBUG_BODY API_DEBUG_BODY CREATE_POST
@Composable
fun CreatePost(
    navController: NavHostController ?=null,) {
    val space = LocalSpacing.current
    val context = LocalContext.current

    val universityService = remember { UniversityService() }
    var subjects by remember { mutableStateOf(listOf<Subject>()) }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = universityService.getAllSubjects()
            subjects = result
        }
    }

    var mSelectedSubject by remember { mutableStateOf("") }
    var mSelectedSubjectName by remember { mutableStateOf("") }
    var mTopic by remember { mutableStateOf("") }
    var mDescription by remember {mutableStateOf("")}
    var mExpirationDate by remember { mutableStateOf(LocalDate.now()) }
    var mUseLocation by remember { mutableStateOf(false) }
    var mLat by remember { mutableDoubleStateOf(0.0) }
    var mLng by remember { mutableDoubleStateOf(0.0) }

    val permission = Manifest.permission.ACCESS_FINE_LOCATION
    fun handlePushingPost(){
        CoroutineScope(Dispatchers.IO).launch {
            val formattedDate = "${mExpirationDate}T00:00:00Z"
            val body = mutableMapOf<String, Any>(
                "title" to mTopic,
                "subjectID" to mSelectedSubject,
                "subject" to mSelectedSubjectName,
                "topic" to mTopic,
                "useProximity" to mUseLocation,
                "expirationDate" to formattedDate
            )

            if (mUseLocation) {
                body["xCoord"] = mLat
                body["yCoord"] = mLng

            }

            Log.e("POST_BODY", "ERROR: $body")

            val newPost = handleApiReqPost("/post/create", body as Map<String, Any>?, context )

            if (newPost.ok) {
                Log.d("CREATE_POST", "OK: ${newPost.code}, ${newPost.content}")
                withContext(Dispatchers.Main) {
                    navController?.navigate("home")
                }
                } else {
                Log.e("CREATE_POST", """
                POST FAILED
                Status code: ${newPost.code}
                Response ok?: ${newPost.ok}
                Raw content: ${newPost.content}
                """.trimIndent())

            }
        }
    }

    val activity = context as? ComponentActivity

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            activity?.let { act ->
                val client = LocationServices.getFusedLocationProviderClient(act)
                @SuppressLint("MissingPermission")
                client.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null
                ).addOnSuccessListener { loc ->
                    if (loc != null) {
                        mLat = loc.latitude
                        mLng = loc.longitude
                        mUseLocation = true
                    } else {
                        Log.w("Location", "Location unavailable")
                    }
                }
            }
        } else {
            mUseLocation = false
            Log.w("Location", "Permission denied")
        }
    }

    if (ContextCompat.checkSelfPermission(context, permission)
        == PackageManager.PERMISSION_GRANTED
    ) {
        activity?.let { act ->
            val client =
                LocationServices.getFusedLocationProviderClient(act)
            @SuppressLint("MissingPermission")
            client.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).addOnSuccessListener { loc ->
                if (loc != null) {
                    mLat = loc.latitude
                    mLng = loc.longitude
                    mUseLocation = true
                    Log.i("CHECKBOX", ".addOnSuccessListener")
                }
            }
        }
    }

    Layout(
        navController= navController,
        showFilter = true,
        pageDetails = { PageHeadline(text = "Create post") },
        content =  {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Subject", fontSize = 20.sp)
                Select(
                    onChange = { selectedId ->
                        mSelectedSubject = selectedId
                        val obj = subjects.find { it.firestoreID == selectedId }
                        mSelectedSubjectName = obj?.name ?: ""
                    },
                    options = subjects.map { subject ->
                        mapOf(
                            "id" to (subject.firestoreID ?: ""),
                            "name" to ("${subject.code} - ${subject.name}")
                        )
                    }
                )
                TextInput(
                    value = mTopic,
                    onChange = { mTopic = it },
                    label = "Assignment/topic")

                TextArea(
                    value = mDescription,
                    label = "Description"
                )

                DateInput(
                    date = mExpirationDate,
                    onDate = { mExpirationDate = it },
                    label = "Post expiration"
                )

                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(space.s),
                    modifier = Modifier
                        .fillMaxWidth()
                        // make entire row clickable so users may tap the label
                        .clickable(
                            onClick = { mUseLocation = !mUseLocation },
                            role = Role.Checkbox
                        )
                ) {

                    Checkbox(
                        modifier = Modifier
                            .width(space.m)
                            .height(space.m),
                        checked = mUseLocation,
                        onCheckedChange = {
                            if (!mUseLocation) {
                                if (ContextCompat.checkSelfPermission(context, permission)
                                    == PackageManager.PERMISSION_GRANTED
                                ) {

                                    // Already granted, just fetch location
                                    activity?.let { act ->
                                        val client =
                                            LocationServices.getFusedLocationProviderClient(act)
                                        @SuppressLint("MissingPermission")
                                        client.getCurrentLocation(
                                            Priority.PRIORITY_HIGH_ACCURACY,
                                            null
                                        ).addOnSuccessListener { loc ->
                                            if (loc != null) {
                                                mLat = loc.latitude
                                                mLng = loc.longitude
                                                mUseLocation = true
                                                Log.i("CHECKBOX", ".assOnSuccessListener")
                                            }
                                        }
                                    }
                                } else {
                                    // Trigger permission prompt manually
                                    permissionLauncher.launch(permission)
                                }
                            } else {
                                mUseLocation = false
                            }
                        },
                    )

                    Text(text = "Visible only to those close to your location",fontSize = 18.sp)
                }
            }


        },
        footer = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space.xs)
            ) {
                AppButton(
                    modifier = Modifier.weight(1f),
                    text = "Cancel",
                    onClick = { navController?.navigate("home") },
                    type = ButtonType.DANGER
                )
                AppButton(
                    modifier = Modifier.weight(1f),
                    text = "Publish",
                    onClick = { handlePushingPost() }
                )
            }
        }
    )
}