package components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize


/**
 * Dropdown menu
 *
 * Options will be provided by parsing JSON. Data MUST have at least
 * "name" and "id" as parameters for it to be displayed here.
 *
 * @param onChange callback function when an option is selected
 * @param options different options to select from
 */
@Composable
fun Select(
    onChange: ((String) -> Unit)? = null,
    options: List<Map<String, String>> = listOf()
    ) {

    var mExpanded by remember { mutableStateOf(false) }
    var mSelectedText by remember { mutableStateOf("") }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(Modifier.padding(20.dp)) {
        OutlinedTextField(
            value = mSelectedText,
            onValueChange = { mSelectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                // makes the items in the dropdown as large as this box
                .onGloballyPositioned { coordinates ->
                    mTextFieldSize = coordinates.size.toSize()
                },
            // readonly for now, as no search functionality will(?) be added
            readOnly = true,
            label = {Text("Select option")},
            trailingIcon = {
                Icon(icon,"contentDescription",
                    Modifier.clickable { mExpanded = !mExpanded })
            }
        )

        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
        ) {
            options.forEach { option ->
                val name = option["name"]
                val id = option["id"]

                // we should only display non-broken data, where we have both an id and a name
                if (name != null && id != null) {
                    DropdownMenuItem(
                        text = { Text(text = name) },
                        onClick = {
                            mSelectedText = name
                            onChange?.invoke(id)
                            mExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SelectDemo () {


    var option by remember { mutableStateOf("") }

    Box (
        // gives some space for the menu to properly show up under Select
        modifier = Modifier.padding(bottom = 600.dp)
    ) {
        Text(option)
        Select(
            onChange = { option = it }, // equivalent to { id -> option = id }

            // in a normal case, "options" would be parsed JSON
            options = listOf(
            mapOf(
                Pair("id", "1234"),
                Pair("more_data", "what"),
                Pair("name", "NTNU")
            ),
            mapOf(
                Pair("name", "Oslo Met"),
                Pair("id", "67")
            ),
            mapOf(
                Pair("name", "Høyskolen Kristiania"),
                Pair("id", "9876"),
                Pair("more_data", "omg"),
                Pair("even_more_data", "no way")
            ),
        ))
    }
}