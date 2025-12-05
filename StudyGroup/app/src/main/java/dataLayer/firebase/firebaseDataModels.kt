package dataLayer.firebase

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName


// UNIVERSITY
data class University(
    val abbr: String? = null,
    val name: String? = null,
    val studyprogram: List<DocumentReference>? = null
)

// STUDY PROGRAM
data class StudyProgram(
    val abbr: String? = null,
    val name: String? = null,

    val posts: Any? = null,

    @get:PropertyName("universityID")
    val universityId: String? = null
)


// SUBJECT
data class Subject(
    val code: String? = null,
    val name: String? = null,

    @get:PropertyName("universityID")
    @set:PropertyName("universityID")
    var universityId: DocumentReference? = null,

    @get:PropertyName("studyPrograms")
    val studyPrograms: List<StudyProgramLink>? = null,

    val posts: List<String>? = null,

    var firestoreID: String? = null
)



// Inner class: elements inside "studyPrograms" array in subjects
data class StudyProgramLink(
    val obligatory: Boolean? = null,

    @get:PropertyName("studyProgramID")
    @set:PropertyName("studyProgramID")
    var studyProgramId: DocumentReference? = null
)




