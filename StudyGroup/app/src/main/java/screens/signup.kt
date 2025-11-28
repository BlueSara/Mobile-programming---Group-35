package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing
import components.AppButton
import components.ButtonType
import components.DateInput
import components.DropDown
import components.PopUp
import components.Select
import components.TextInput
import java.time.LocalDate

var userMap = mutableMapOf(
    "firstName" to "",
    "lastName" to "",
    "email" to "",
    "password" to "",
    "confirmedPassword" to "",
    "universityID" to "",
    "studyProgram" to mutableMapOf<String, Any>(),
    "subjects" to mutableListOf<Map<String, Any>>(),
    "startDate" to LocalDate.now(),
    "initialEndDate" to LocalDate.now(),
)



@Composable
//@Preview(showBackground = true)
fun SignupCredentials(
    navController: NavHostController ?=null)
{
    var mFirstname by remember { mutableStateOf(userMap["firstName"]) }
    var mLastname by remember { mutableStateOf(userMap["lastName"]) }
    var mUniEmail by remember { mutableStateOf(userMap["email"]) }
    var mPassword by remember { mutableStateOf(userMap["password"]) }
    var mPasswordConfirmation by remember { mutableStateOf(userMap["confirmedPassword"]) }
    var mAffiliationID by remember { mutableStateOf(userMap["universityID"]) }

    val space = LocalSpacing.current
    val colors = LocalCustomColors.current

    fun navigateNextScreen(){
        if (mFirstname == "") return
        if (mLastname == "") return
        if (mUniEmail == "") return
        if (mPassword == "") return
        if (mPasswordConfirmation == "") return
        if (mAffiliationID == "") return

        userMap["firstName"] = mFirstname
        userMap["lastName"] = mLastname
        userMap["email"] = mUniEmail
        userMap["password"] = mPassword
        userMap["confirmedPassword"] = mPasswordConfirmation
        navController?.navigate("signUpUniversity")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(space.s, space.m)
    ) {
        Text(
            text = "Welcome to Study Group!",
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top=space.xl)
        )
        Row(
            modifier = Modifier
                .padding(top = space.xl, start = 135.dp, bottom = space.l)
                .fillMaxWidth(1f)
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ){
            Text(
                text = "Sign up",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
            )
            Text(
                text="1/2",
                fontSize = 20.sp,
                color = colors.grey,
                modifier = Modifier
                    .height(33.dp)
                    .padding(top = 8.dp)
                    .align(Alignment.CenterVertically)
                ,
            )
        }

        TextInput(
            value = mFirstname?.toString() ?: "",
            onChange = { mFirstname = it },
            label = "First name"
        )
        TextInput(
            value = mLastname?.toString() ?: "",
            onChange = { mLastname = it },
            label = "Last name"
        )
        TextInput(
            value = mUniEmail?.toString() ?: "",
            onChange = { mUniEmail = it },
            label = "University email"
        )
        TextInput(
            value = { "*".repeat((mPassword?.toString() ?: "").length) }(),
            onChange = { mPassword = it },
            label = "Password",
            maxLength = 100
        )
        TextInput(
            value = { "*".repeat((mPasswordConfirmation?.toString() ?: "").length) }(),
            onChange = { mPasswordConfirmation = it },
            label = "Confirm password",
            maxLength = 100
        )

        Text(
            text = "Choose affiliation",
            style = TextStyle(
                fontSize = 20.sp,
            ),
            modifier = Modifier
                .padding(top = space.m)
        )
        Select(
            options = listOf(   // TODO: CHANGE THIS TO BE REAL DATA
                mapOf(
                    Pair("id", "1234"),
                    Pair("name", "NTNU")
                ),
                mapOf(
                    Pair("name", "Oslo Met"),
                    Pair("id", "67")
                ),
                mapOf(
                    Pair("name", "Høyskolen Kristiania"),
                    Pair("id", "9876")
                )
            ),
            onChange = {it -> mAffiliationID = it}
        )
        Text(
            text="Already have an account? Sign in here!",
            color = colors.primaryActive,
            textDecoration = TextDecoration.Underline,
            fontSize = 18.sp,
            modifier =  Modifier
                .padding(top = space.s)
                .clickable(
                    onClick = {navController?.navigate("signIn")}
                )

        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            AppButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "NEXT",
                onClick = {navigateNextScreen()}
            )
        }
    }
}

//mockup data for subjects
var mockupSubjects = mutableListOf(
    mapOf(
        "subjectID" to "someID1",
        "universityID" to "someUni",
        "subjectName" to "Mobile programming",
        "subjectCode" to "prog2007"
    ),
    mapOf(
        "subjectID" to "someID2",
        "universityID" to "someUni",
        "subjectName" to "Web of things",
        "subjectCode" to "IDG3004"
    ),
    mapOf(
        "subjectID" to "someID3",
        "universityID" to "someUni",
        "subjectName" to "Modern web sustainability",
        "subjectCode" to "IDG3000"
    ),
    mapOf(
        "subjectID" to "someID4",
        "universityID" to "someUni",
        "subjectName" to "In depth project",
        "subjectCode" to "IDG3002"
    )
)

var mockupStudyPrograms = mutableListOf(
    mapOf(
        "studyProgramID" to "someProgramID1",
        "universityID" to "someUni",
        "studyProgramName" to "Bachelor in programming",
        "studyProgramAbbr" to "BPROG"
    ),
    mapOf(
        "studyProgramID" to "someProgramID2",
        "universityID" to "someUni",
        "studyProgramName" to "Bachelor in web development",
        "studyProgramAbbr" to "BWU"
    ),
    mapOf(
        "studyProgramID" to "someProgramID3",
        "universityID" to "someUni",
        "studyProgramName" to "Bachelor in interaction design",
        "studyProgramAbbr" to "BIXD"
    ),
    mapOf(
        "studyProgramID" to "someProgramID4",
        "universityID" to "someUni",
        "studyProgramName" to "Bachelor in don't hakk plis",
        "studyProgramAbbr" to "DIGSEC"
    ),
)

/** Fetches study-programs for selected university from the api and returns them
 * */
fun fetchStudyPrograms(): MutableList<Map<String, Any>>{
    //TODO : FILL WITH LOGIC TO FETCH STUDY PROGRAMS
 return mockupStudyPrograms.toMutableStateList()
}
//To fetch subjects, currently just with placeholder logic
/**Fetches subjects relevant to university from the api and returns them
 * */
fun fetchSubjects() : MutableList<Map<String, Any>>{
    //TODO : FILL WITH ACTUAL LOGIC TO FETCH RELEVANT SUBJECTS
    return mockupSubjects.toMutableStateList()
}

@Composable
@Preview(showBackground = true)
fun SignupUniversity(
    navController: NavHostController ?=null)
{
    val mStudyProgram = remember {
        val original = userMap["studyProgram"] as? Map<String, Any>
        if (original != null) mutableStateMapOf<String, Any>().apply { original.forEach { (k,v) -> put(k, v) } }
        else mutableStateMapOf()
    }
    var mDateStarted by remember {mutableStateOf((userMap["startDate"] as? LocalDate)?: LocalDate.now()) }
    var mGraduation by remember {mutableStateOf((userMap["initialEndDate"] as? LocalDate)?: LocalDate.now()) }
    val mAllSubjects = remember { fetchSubjects().toMutableStateList() }
    val mCurrentSubjects = remember{ ((userMap["subjects"] as? List<Map<String, Any>>) ?: mutableStateListOf()).toMutableStateList() }
    val mStudyPrograms = remember { fetchStudyPrograms().toMutableStateList()}
    val mNewSubject = remember { mutableStateMapOf<String, Any>() }
    var mShowSubjectDropDown by remember { mutableStateOf(false) }
    var mShowStudyDropDown by remember { mutableStateOf(false) }
    var mStudyProgramsOptions = remember { mutableListOf<Map<String, Any>>().toMutableStateList() }
    var mSubjectOptions = remember { mutableListOf<Map<String, Any>>().toMutableStateList() }
    var mShowPopUp by remember { mutableStateOf(false) }

    val space = LocalSpacing.current
    val colors = LocalCustomColors.current

    fun handleStudyProgramOptions(){
        mStudyProgramsOptions = when(mStudyProgram["studyProgramAbbr"]){
            null -> mStudyPrograms
            else -> mStudyPrograms.filter { it ->
                it["studyProgramAbbr"]
                    ?.toString()
                    ?.contains(other= (mStudyProgram["studyProgramAbbr"]?: "") as CharSequence, ignoreCase = true ) == true
            }.toMutableStateList()
        }
    }

    fun handleSubjectsOptions(){
        mSubjectOptions = when(mNewSubject["subjectCode"]){
            null -> mAllSubjects
            else -> mAllSubjects.filter { it ->
                it["subjectCode"]
                    ?.toString()
                    ?.contains(other= mNewSubject["subjectCode"]?.toString()?: "", ignoreCase = true ) == true
            }.toMutableStateList()
        }
    }

    /**Handling that adding new subjects.
     * Checks that subject name and code is not empty,
     * and that "obligatory" is not null
     * @return Boolean - Returns boolean regarding whether the new subject could be added or not.
     * */
    fun addNewSubject(): Boolean {
        if (mNewSubject["subjectName"]?.toString().isNullOrEmpty()) return false
        if (mNewSubject["subjectCode"]?.toString().isNullOrEmpty()) return false
        if (mNewSubject["obligatory"] == null) return false
        if (mNewSubject["obligatory"] == false && mNewSubject["partOfStudyProgram"] == null){
            mNewSubject["partOfStudyProgram"] = true
        }
        mCurrentSubjects.add(mNewSubject.toMutableMap())
        mNewSubject.clear()
        return true
    }

    fun storeNewUser(){
        userMap["studyProgram"] = mutableMapOf(
            "studyProgramName" to mStudyProgram["studyProgramName"],
            "studyProgramAbbr" to mStudyProgram["studyProgramAbbr"],
            "studyProgramID" to mStudyProgram["studyProgramID"]
        )
        userMap["subjects"] = mCurrentSubjects
        userMap["startDate"] = mDateStarted
        userMap["initialEndDate"] = mGraduation
    }

    fun handleSignup(){
        //just placeholder variable to be able to have more content
        /*TODO : ADD PROPER LOGIC FOR SIGNING UP. Should call api to get response*/
        var signupSuccess = true
        if (mStudyProgram["studyProgramName"]?.toString().isNullOrEmpty()) signupSuccess = false
        if (mStudyProgram["studyProgramAbbr"]?.toString().isNullOrEmpty()) signupSuccess = false
        storeNewUser()

        if (!signupSuccess) return
        navController?.navigate("home")

    }

    handleStudyProgramOptions()
    handleSubjectsOptions()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(space.s, space.m)
    ) {
        Text(
            text = "Welcome to Study Group!",
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top=space.xl)
        )
        Row(
            modifier = Modifier
                .padding(top = space.xl, start = 135.dp, bottom = space.l)
                .fillMaxWidth(1f)
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ){
            Text(
                text = "Sign up",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
            )
            Text(
                text="2/2",
                fontSize = 20.sp,
                color = colors.grey,
                modifier = Modifier
                    .height(33.dp)
                    .padding(top = 8.dp)
                    .align(Alignment.CenterVertically)
                ,
            )
        }

        Column() {
            TextInput(
                value = "${mStudyProgram["studyProgramAbbr"]?: ""}",
                onChange =
                    { s ->
                        mShowStudyDropDown = true
                        mStudyProgram["studyProgramAbbr"] = s
                        handleStudyProgramOptions()
                    },
                label = "Study program abbreviation",
                maxLength = 10
            )
            if (mShowStudyDropDown) {
                Popup(

                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = space.s + space.xs)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(space.xl * 2 - space.xs)
                        )
                        DropDown(
                            onDismiss = { mShowStudyDropDown = false },
                            options = mStudyProgramsOptions,
                            text = "Add study program ${mStudyProgram["studyProgramAbbr"]}",
                            onSelect = { it ->
                                mStudyProgram.putAll(it as Map<out String, String>)
                                mShowStudyDropDown = false
                            }
                        )
                    }
                }
            }
        }
        TextInput(
            value = "${mStudyProgram["studyProgramName"]?:""}",
            onChange = { mStudyProgram["studyProgramName"] = it },
            label = "Study program name",
            maxLength = 50
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = space.m)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = space.s),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ){
                Text(
                    text = "Your subjects",
                    fontSize = 20.sp
                )
                AppButton(
                    text = "+",
                    type = ButtonType.SUCCESS,
                    width = space.xl,
                    onClick = {mShowPopUp = true}
                )
            }

            FlowRow(
                modifier = Modifier
                    .border(
                        1.dp,
                        color = colors.borderColor,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .height(space.xl * 3)
                    .padding(space.s)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(space.xs),
                horizontalArrangement = Arrangement.spacedBy(space.xs)
            ) {
                mCurrentSubjects.forEachIndexed { i, s ->
                    Row(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(3.dp))
                            .background(colors.foreground)
                            .padding(space.s, space.xs)
                            .fillMaxWidth(0.447f)
                        ,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "${s["subjectCode"]}", fontSize = 18.sp
                        )
                        AppButton(
                            type = ButtonType.DANGER,
                            width = space.xl,
                            text = "X",
                            onClick = { mCurrentSubjects.removeAt(i)}
                        )
                    }
                }
            }
        }

        DateInput(
            date = mDateStarted,
            onDate = { mDateStarted = it },
            label = "Study program start"
        )
        DateInput(
            date = mGraduation,
            onDate = { mGraduation = it },
            label = "Study program end"
        )

        Text(
            text="Already have an account? Sign in here!",
            color = colors.primaryActive,
            textDecoration = TextDecoration.Underline,
            fontSize = 18.sp,
            modifier =  Modifier
                .padding(top = space.s)
                .clickable(
                    onClick = {navController?.navigate("signIn")}
                )

        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space.xs)
            ) {
                AppButton(
                    modifier = Modifier.weight(1f),
                    text = "Go Back",
                    onClick = {
                        storeNewUser()
                        navController?.navigate("signUpCredentials")
                    },
                    type = ButtonType.DANGER
                )
                AppButton(
                    modifier = Modifier.weight(1f),
                    text = "Sign Up",
                    onClick = { handleSignup() }
                )
            }
        }
    }

    if (mShowPopUp){
        PopUp(
            onDismiss = {
                mShowPopUp = false
                mNewSubject.clear()
                        },
            title = "Add subject"
        ) {
            Column(){
                TextInput(
                    value = mNewSubject["subjectCode"]?.toString()?: "",
                    label = "Subject code",
                    onChange =
                        { s ->
                            mShowSubjectDropDown = true
                            mNewSubject["subjectCode"] = s
                            handleSubjectsOptions()
                        },
                )
                if (mShowSubjectDropDown){
                Popup(

                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = space.s + space.xs)
                    ){
                        Spacer(
                            modifier = Modifier
                                .height(space.xl*2 - space.xs)
                        )
                        DropDown(
                            onDismiss = { mShowSubjectDropDown = false },
                            options = mSubjectOptions,
                            text = "Add subject ${mNewSubject["subjectCode"] ?: ""}",
                            onSelect = { subject ->
                                mNewSubject.putAll(subject)
                                mShowSubjectDropDown = false
                            }
                        )
                    }
                }
            }
            }
            TextInput(
                value = mNewSubject["subjectName"]?.toString()?: "",
                onChange = {s -> mNewSubject["subjectName"] = s},
                label = "Subject name",
            )

            Column(
                modifier = Modifier
                    .padding(bottom = space.m),
                verticalArrangement = Arrangement.spacedBy(space.s)
            ) {
                Text(
                    text="Is this subject obligatory?",
                    fontSize = 20.sp
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(space.xs)
                ){
                    AppButton(
                        text="No",
                        type = when (mNewSubject["obligatory"]){
                            false -> ButtonType.DANGERACTIVE
                            else -> ButtonType.DANGER
                        },
                        onClick = { mNewSubject["obligatory"] = false },
                        modifier = Modifier.weight(1f)
                    )
                    AppButton(
                        text="Yes",
                        type = when (mNewSubject["obligatory"]){
                            true -> ButtonType.SUCCESSACTIVE
                            else -> ButtonType.SUCCESS
                        },
                        onClick = { mNewSubject["obligatory"] = true },
                        modifier = Modifier.weight(1f)
                    )
                }

            }
            if (mNewSubject["obligatory"] == false){
                Column(
                    modifier = Modifier
                        .padding(bottom = space.m),
                    verticalArrangement = Arrangement.spacedBy(space.s)
                ) {
                    Text(
                        text="Is this subject part of your study program?",
                        fontSize = 20.sp
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(space.xs)
                    ){
                        AppButton(
                            text="No",
                            type = when (mNewSubject["partOfStudyProgram"]){
                                false -> ButtonType.DANGERACTIVE
                                else -> ButtonType.DANGER
                            },
                            onClick = { mNewSubject["partOfStudyProgram"] = false },
                            modifier = Modifier.weight(1f)
                        )
                        AppButton(
                            text="Yes",
                            type = when (mNewSubject["partOfStudyProgram"]){
                                true -> ButtonType.SUCCESSACTIVE
                                else -> ButtonType.SUCCESS
                            },
                            onClick = { mNewSubject["partOfStudyProgram"] = true },
                            modifier = Modifier.weight(1f)
                        )
                    }

                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = space.m)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = space.s),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ){
                    Text(
                        text = "Your subjects",
                        fontSize = 20.sp
                    )
                }

                FlowRow(
                    modifier = Modifier
                        .border(
                            1.dp,
                            color = colors.borderColor,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .height(space.xl * 4)
                        .padding(space.s)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(space.xs),
                    horizontalArrangement = Arrangement.spacedBy(space.xs)
                ) {
                    mCurrentSubjects.forEachIndexed { i, s ->
                        Row(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(3.dp))
                                .background(colors.foreground)
                                .padding(space.s, space.xs)
                                .fillMaxWidth(0.447f)
                            ,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = "${s["subjectCode"]}", fontSize = 18.sp
                            )
                            AppButton(
                                type = ButtonType.DANGER,
                                width = space.xl,
                                text = "X",
                                //remove item from list when clicking X button
                                onClick = {mCurrentSubjects.removeAt(i)}
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = when(mNewSubject["obligatory"]){
                        true -> space.xl * 4 - space.xs
                        null -> space.xl * 4 - space.xs
                        else -> space.m + space.xs + (11.dp / 5)
                    }
                    ),
                horizontalArrangement = Arrangement.spacedBy(space.xs)
            ){
                AppButton(
                    text="Save and go back",
                    type = ButtonType.PRIMARY,
                    onClick = {
                        val exitPopUp = addNewSubject()
                        //does not close pop-up if the new subject could not be added
                        if (exitPopUp) mShowPopUp = false
                    },
                    modifier = Modifier.weight(1f)
                )
                AppButton(
                    text="Add more",
                    type = ButtonType.SUCCESS,
                    onClick = {
                        addNewSubject()
                    },
                    modifier = Modifier.weight(1f)
                )
            }

        }
    }
}