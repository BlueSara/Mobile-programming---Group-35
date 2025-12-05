package viewModel



/**
 * Handling that adding new subjects.
 * Checks that subject name and code is not empty,
 * and that "obligatory" is not null
 * @return Boolean - Returns boolean regarding whether the new subject could be added or not.
 * */
fun addNewSubject(
    newSubject: MutableMap<String, Any>,
    onSuccess: () -> Unit
): Boolean {
    if (newSubject["subjectName"]?.toString().isNullOrEmpty()) return false
    if (newSubject["subjectCode"]?.toString().isNullOrEmpty()) return false
    if (newSubject["obligatory"] == null) return false
    if (newSubject["obligatory"] == false && newSubject["partOfStudyProgram"] == null){
        newSubject["partOfStudyProgram"] = true
    }
    onSuccess.invoke()
    newSubject.clear()
    return true
}