package screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.studygroup.ui.theme.LocalSpacing
import components.AppButton
import components.ButtonType
import components.DateInput
import components.PageHeadline
import components.Select
import components.TextArea
import components.TextInput
import handleApiReqGet
import handleApiReqPost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import layout.Layout
import java.time.LocalDate
import dataLayer.firebase.UniversityService
import dataLayer.firebase.Subject
import handleApiReqDelete
import handleApiReqPut

@Composable
fun EditPost(
    navController: NavHostController? = null,
    postID: String?
) {
    val space = LocalSpacing.current
    val context = LocalContext.current

    // post data from the  API
    var postData by remember { mutableStateOf<Map<String, Any>?>(null) }

    // Subjects from Firestorehandler
    val universityService = remember { UniversityService() }
    var subjects by remember { mutableStateOf(listOf<Subject>()) }

    var mSelectedSubject by remember { mutableStateOf("") }
    var mSelectedSubjectName by remember { mutableStateOf("") }
    var mTopic by remember { mutableStateOf("") }
    var mDescription by remember { mutableStateOf("") }
    var mExpirationDate by remember { mutableStateOf(LocalDate.now()) }
    var mUseLocation by remember { mutableStateOf(false) }

    // Get all the subjects from firestore
    LaunchedEffect(Unit) {
        try {
            val result = universityService.getAllSubjects()
            subjects = result
        } catch (e: Exception) {
            Log.e("EDIT_POST", "Failed to load subjects: $e")
        }
    }

    // Get the data of post
    LaunchedEffect(postID) {
        if (postID != null) {
            val res = handleApiReqGet("/posts/$postID", context)
            if (res.ok) {
                postData = res.content as? Map<String, Any>
                Log.e("EDIT_POST", "loaded: $postData")
            } else {
                Log.e("EDIT_POST", "Failed to load post: ${res.code} | ${res.content}")
            }
        }
    }

    // update the new data.
    LaunchedEffect(postData) {
        postData?.let { post ->
            mSelectedSubject = post["SubjectID"]?.toString() ?: ""
            mSelectedSubjectName = post["Subject"]?.toString() ?: ""
            mTopic = post["Title"]?.toString() ?: ""
            mDescription = post["Topic"]?.toString() ?: ""
            mUseLocation = post["UseProximity"] as? Boolean ?: false

            val dateRaw = post["ExpirationDate"]?.toString() ?: ""
            if (dateRaw.length >= 10) {
                mExpirationDate = LocalDate.parse(dateRaw.substring(0, 10))
            }
        }
    }

    fun handleUpdatePost() {
        if (postID == null) return

        val formattedDate = "${mExpirationDate}T00:00:00Z"

        val body = mapOf(
            "title" to mTopic,
            "subjectID" to mSelectedSubject,   // firestoreID
            "topic" to mDescription,
            "useProximity" to mUseLocation,
            "expirationDate" to formattedDate
        )

        CoroutineScope(Dispatchers.IO).launch {
            val res = handleApiReqPut("/posts/$postID", context, body)

            if (res.ok) {
                Log.d("EDIT_POST", "Updated successfully")
                withContext(Dispatchers.Main) {
                    navController?.navigate("userPost")
                }
            } else {
                Log.e("EDIT_POST", "Update FAILED: ${res.code} | ${res.content}")
            }
        }
    }

    fun handleDeletePost() {
       if (postID == null) return

       CoroutineScope(Dispatchers.IO).launch {
           val res = handleApiReqDelete("/post/$postID", context)

           if (res.ok) {
               Log.d("EDIT_POST", "Delete successfull!")
               withContext(Dispatchers.Main) {
                   navController?.navigate("userPost")
               }
           } else {
               Log.e("EDIT_POST", "DELETE FAIL :( ${res.code} | ${res.content}")
           }
       }
    }

    Layout(
        navController = navController,
        showFilter = true,
        pageDetails = { PageHeadline(text = "Edit post") },
        content = {
            Column(
                modifier = Modifier.fillMaxWidth()
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
                    label = "Assignment/topic"
                )

                TextArea(
                    value = mDescription,
                    onChange = { mDescription = it },
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
                        onCheckedChange = { mUseLocation = !mUseLocation },
                    )

                    Text(
                        text = "Visible only to those close to your location",
                        fontSize = 18.sp
                    )
                }
                Box(
                    modifier = Modifier.padding(top=space.xl*2)
                ){

                    AppButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Delete post",
                        type = ButtonType.DANGER,
                        onClick = { handleDeletePost() }
                    )
                }
            }

        },
        footer = {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                    onClick = { handleUpdatePost() }
                )
            }
        }
    )
}
