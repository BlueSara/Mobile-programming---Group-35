package database

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestFirestoreScreen() {
    val service = remember { UniversityService() }
    val scope = rememberCoroutineScope()

    var universities by remember { mutableStateOf(emptyList<University>()) }
    var studyPrograms by remember { mutableStateOf(emptyList<StudyProgram>()) }
    var subjects by remember { mutableStateOf(emptyList<Subject>()) }

    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    fun loadData() {
        loading = true
        error = null

        scope.launch {
            try {
                universities = service.getAllUniversities()
                studyPrograms = service.getAllStudyPrograms()
                subjects = service.getAllSubjects()

            } catch (e: Exception) {
                error = e.message
                e.printStackTrace()
            } finally {
                loading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadData()
    }

    Column(Modifier.padding(16.dp)) {

        Button(onClick = { loadData() }) {
            Text("REFRESH TESTS")
        }

        Spacer(Modifier.height(16.dp))

        if (loading) Text("Loading…")
        error?.let { Text("Error: $it", color = MaterialTheme.colorScheme.error) }

        Text("Universities (${universities.size})")
        universities.forEach {
            Text(" - ${it.abbr} : ${it.name}")
        }

        Spacer(Modifier.height(20.dp))

        Text("Study Programs (${studyPrograms.size})")
        studyPrograms.forEach {
            Text(" - ${it.abbr} : ${it.name}")
        }

        Spacer(Modifier.height(20.dp))

        Text("Subjects (${subjects.size})")
        subjects.forEach {
            Text(" - ${it.code} : ${it.name}")
        }
    }
}
