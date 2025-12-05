package dataLayer.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class UniversityService {
    // Get firestore instance
    private val db = FirebaseFirestore.getInstance()
    // Reference to university collection
    private val universityRef = db.collection("university")

    // Reference to studyProgram collection
    private val studyProgramRef = db.collection("studyProgram")

    // Reference to subjects collection
    private val subjectRef = db.collection("subjects")

    /**
     * Retrieves all universities from the university collection in firestore.
     *
     * Uses .await() to suspend until the query finishes.
     * Maps each document to a University object using Firestore’s toObject().
     *
     * @return A list of [University] objects, or an empty list if an error occurs.
     */
    suspend fun getAllUniversities(): List<University> {
        return try {
            universityRef
                .get()          // Firestore query
                .await()
                .documents
                .mapNotNull { it.toObject<University>() }   // Parse to data class
        } catch (e: Exception) {    // Error handling if there is nothing to retrieve
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Retrieves all study programs from the studyProgram collection.
     *
     * Uses Firestore's toObject() to parse documents into [StudyProgram] objects.
     *
     * @return A list of [StudyProgram] objects or an empty list on failure.
     */
    suspend fun getAllStudyPrograms(): List<StudyProgram> {
        return try {
            studyProgramRef
                .get()      // Firestore query
                .await()
                .documents
                .mapNotNull { it.toObject<StudyProgram>() }   // Parse to data class
        } catch (e: Exception) {    // Error handling if there is nothing to retrieve
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Retrieves all subjects from the subjects collection.
     *
     * Parses Firestore documents into [Subject] objects using toObject().
     *
     * @return A list of [Subject] objects or an empty list on failure.
     */
    suspend fun getAllSubjects(): List<Subject> {
        return try {
            subjectRef
                .get()      // Firestore query
                .await()
                .documents
                .mapNotNull { it.toObject<Subject>() }  // Parse to data class
        } catch (e: Exception) {    // Error handling if there is nothing to retrieve
            e.printStackTrace()
            emptyList()
        }
    }


    /**
     * Retrieves all subjects associated with a specific study program.
     *
     * The function loads all subjects, converts them to [Subject] objects,
     * and filters them by checking if the subjects studyProgram links
     * contain the provided study program ID.
     *
     * @param studyProgId The ID of the study program to match against.
     *
     * @return A list of [Subject] objects that belong to the given study program.
     * Returns an empty list if no matches are found or an error occurs.
     */
    suspend fun getSubjectsForStudyProgram(studyProgId: String): List<Subject> {
        return try {
            val snapshot = subjectRef.get().await()

            snapshot.documents
                .mapNotNull { it.toObject<Subject>() }  // Parse to data class
                // Check if any study program link matches
                .filter { subject ->
                    subject.studyPrograms?.any { link ->
                        link.studyProgramId?.id == studyProgId
                    } == true
                }

        } catch (e: Exception) {    // Error handling if there is nothing to retrieve
            e.printStackTrace()
            emptyList()
        }
    }
}
