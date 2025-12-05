package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import components.PageHeadline
import components.PopUp
import components.TextInput
import layout.Layout
import viewModel.addNewSubject
import java.time.LocalDate

@Composable
fun UserAccount(
    navController: NavHostController ?=null,){
    var mFirstName by remember { mutableStateOf("") }
    var mLastName by remember { mutableStateOf("") }
    var mShowStudyDropDown by remember { mutableStateOf(false) }
    val mAllSubjects = remember { fetchSubjects().toMutableStateList() }
    val mStudyPrograms = remember { fetchStudyPrograms().toMutableStateList()}
    val mStudyProgram = remember {mutableStateMapOf<String, Any>()}
    val mNewSubject = remember { mutableStateMapOf<String, Any>() }
    var mDateStarted by remember { mutableStateOf(LocalDate.now()) }
    var mGraduation by remember { mutableStateOf(LocalDate.now()) }
    var mShowSubjectDropDown by remember { mutableStateOf(false) }
    var mStudyProgramsOptions = remember { mutableStateListOf<Map<String, Any>>().toMutableStateList() }
    var mSubjectOptions = remember { mutableStateListOf<Map<String, Any>>().toMutableStateList() }
    var mShowPopUp by remember { mutableStateOf(false) }
    val mCurrentSubjects = remember{ mutableStateListOf<Map<String, Any>>().toMutableStateList() }

    val space = LocalSpacing.current
    val colors = LocalCustomColors.current

    fun handleFetchUserAccount(){
        //TODO : implement logic to fetch the users account details
    }

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

/*
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
*/
    fun handleUpdateAccount(){
        /*TODO :
           Add logic for some minor front-end validation and calling api.
           information needs to be structured into proper map before calling api.
           Minor front-end validation:
           - just to avoid calling api if any fields are empty

        *  */

        val success = true

        //TODO : Add logic for showing "err" popup when relevant
        if (!success) return
        navController?.navigate("home")
    }

    Layout(
        navController= navController,
        pageDetails = { PageHeadline(text="Account") },
        footer ={
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                AppButton(
                    modifier = Modifier.weight(1f),
                    type = ButtonType.PRIMARY,
                    text = "Save and update",
                    onClick = {handleUpdateAccount()}
                )
            }
        },
        content = {
            TextInput(
                label = "First name",
                value = mFirstName,
                onChange = {it -> mFirstName = it}
            )
            TextInput(
                label = "Last name",
                value = mLastName,
                onChange = {it -> mLastName = it}
            )

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
                label = "Graduation date"
            )
        }
    )
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
                        val exitPopUp = addNewSubject(mNewSubject, { mCurrentSubjects.add(mNewSubject.toMutableMap()) })
                        //does not close pop-up if the new subject could not be added
                        if (exitPopUp) mShowPopUp = false
                    },
                    modifier = Modifier.weight(1f)
                )
                AppButton(
                    text="Add more",
                    type = ButtonType.SUCCESS,
                    onClick = {
                        addNewSubject(mNewSubject, { mCurrentSubjects.add(mNewSubject.toMutableMap()) })
                    },
                    modifier = Modifier.weight(1f)
                )
            }

        }
    }
}