package components

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygroup.ui.theme.LocalCustomColors
import com.example.studygroup.ui.theme.LocalSpacing

@Composable
fun DropDown(
    onSelect: ((Map<String, Any>) -> Unit)? = null,
    onDismiss: (() -> Unit)?= null,
    options: List<Map<String, Any>> = emptyList(),
    text: String ?= "<text>"
){

    val colors = LocalCustomColors.current
    val space = LocalSpacing.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .clip(
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 4.dp,
                    bottomEnd = 4.dp
                )
            )
            .border(
                width = 0.dp, color = colors.white, RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 4.dp,
                    bottomEnd = 4.dp
                )
            )
        ,
    ) {
        Column(
            modifier = Modifier
                .padding(space.xs)
                .heightIn(min = space.xl * 2, max = space.xl * 5)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(space.xs)

        ) {
            options.forEach {
                Button(
                    onClick = {onSelect?.invoke(it)},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(space.xl + space.xs)
                    ,
                    contentPadding = PaddingValues(vertical = 0.dp, horizontal = space.s),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.info,
                        contentColor = colors.black,
                    ),

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${it["code"] ?: ""} - ${it["name"]?: ""}",
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "+",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
        Button(
            onClick = {onDismiss?.invoke()},
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 4.dp
                    )
                )
                .height(space.xl)
            ,
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp,
                bottomStart = 4.dp,
                bottomEnd = 4.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.white,
            ),
            contentPadding = PaddingValues(vertical = 0.dp, horizontal = space.s),
        ){
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    fontSize = 18.sp,
                    text ="$text"
                )
            }
        }
    }

}

@SuppressLint("MutableCollectionMutableState")
@Preview(showBackground = true)
@Composable
fun PreviewDropDown(){
    //some mockup data
    val someList : MutableList<Map<String, Any>> = mutableListOf(
        mapOf(
            "id" to "someID1",
            "code" to "someCode1",
            "name" to "someName1"
        ),
        mapOf(
            "id" to "someID2",
            "code" to "someCode2",
            "name" to "someName2"
        ),
        mapOf(
            "id" to "someID3",
            "code" to "someCode3",
            "name" to "someName3"
        ),
        mapOf(
            "id" to "someID4",
            "code" to "someCode4",
            "name" to "someName4"
        ),
        mapOf(
            "id" to "someID5",
            "code" to "someCode5",
            "name" to "someName5"
        ),
        mapOf(
            "id" to "someID6",
            "code" to "someCode6",
            "name" to "someName6"
        )
    )


    //some mockup state for selected item
    var selectedItem by remember { mutableStateOf(emptyMap<String, Any>()) }
    var options by remember { mutableStateOf(someList.toMutableList())}

    //updating the text-value inserted and the list based on text-input
    fun updateTextValue(value: String = ""){

        //updating of the text displayed (text-value)
        selectedItem = selectedItem
        .toMutableMap()
            .apply {
                this["code"] = value
            }

        //updating the options that can be selected from based on text-field value
        options = if (value != ""){
            someList
                .filter { (it["code"] as? String)
                    ?.lowercase()
                    ?.contains(value) == true
                }
                .toMutableList()
        } else someList
    }

    Column()
    {
        //just to make it possible to see with interaction
        TextInput(
            label = "Just a placeholder text-input for demonstration",
            value = "${selectedItem["code"]?: ""}",
            onChange = {value -> updateTextValue(value)}
            )

        //just to display the selected item
        Text(text = "${(selectedItem["code"]?:"")} ${selectedItem["name"]?: "No item selected"} ${selectedItem["id"]?: ""}")
        DropDown(
            text = "Add subject: ${selectedItem["code"]?: ""}",
            onSelect = {item -> selectedItem = item},
            options = options,
            onDismiss = {selectedItem = emptyMap()}
        )
    }
}