package database

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await




// This is just a test for the firebasehandler
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestFirestoreScreen() {
    val service = remember { UniversityService() }
    val scope = rememberCoroutineScope()

    var universities by remember { mutableStateOf<List<University>>(emptyList()) }
    var studyPrograms by remember { mutableStateOf<List<StudyProgram>>(emptyList()) }
    var subjects by remember { mutableStateOf<List<Subject>>(emptyList()) }

    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    fun loadData() {
        loading = true
        error = null

        scope.launch {
            try {
                // Get all universities
                universities = service.getAllUniversities()

                val uni = universities.firstOrNull { it.studyprogram?.isNotEmpty() == true }
                val studyProgRefs = uni?.studyprogram ?: emptyList()

                // Fetch study programs from DocumentReferences
                studyPrograms = studyProgRefs.mapNotNull { ref ->
                    ref.get().await().toObject<StudyProgram>()
                }

                // Fetch subjects for the FIRST study program
                val firstProgRef = studyProgRefs.firstOrNull()
                if (firstProgRef != null) {
                    subjects = service.getSubjectsForStudyProgram(firstProgRef.id)
                }

            } catch (e: Exception) {
                error = e.message
                e.printStackTrace()
            } finally {
                loading = false
            }
        }
    }

    LaunchedEffect(true) {
        loadData()
    }

    Column(Modifier.padding(16.dp)) {
        Button(onClick = { loadData() }) {
            Text("REFRESH TESTS")
        }

        Spacer(Modifier.height(16.dp))

        if (loading) Text("Loading…")
        error?.let { Text("Error: $it", color = MaterialTheme.colorScheme.error) }


        // Display Universities

        Text("Universities (${universities.size})")
        universities.forEach {
            Text(" - ${it.abbr} : ${it.name}")
        }

        Spacer(Modifier.height(20.dp))


        // Study Programs

        Text("Study Programs (${studyPrograms.size})")
        studyPrograms.forEach {
            Text(" - ${it.abbr} : ${it.name}")
        }

        Spacer(Modifier.height(20.dp))

        // Subjects

        Text("Subjects (${subjects.size})")
        subjects.forEach {
            Text(" - ${it.code} : ${it.name}")
        }
    }
}
