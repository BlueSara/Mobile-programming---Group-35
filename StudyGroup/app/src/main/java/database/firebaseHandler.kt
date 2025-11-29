package database

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class UniversityService {

    private val db = FirebaseFirestore.getInstance()
    private val universityRef = db.collection("university")
    private val subjectRef = db.collection("subjects")


    // GET ALL universities
    suspend fun getAllUniversities(): List<University> {
        return try {
            universityRef
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject<University>() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


    // GET subjects for one study program
    suspend fun getSubjectsForStudyProgram(studyProgId: String): List<Subject> {
        return try {
            val snapshot = subjectRef.get().await()

            snapshot.documents
                .mapNotNull { it.toObject<Subject>() }
                .filter { subject ->
                    subject.studyPrograms?.any { link ->
                        link.studyProgramId?.id == studyProgId
                    } == true
                }

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
