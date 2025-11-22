package components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studygroup.ui.theme.LocalSpacing


/**
 * Dropdown menu
 *
 * Options will be provided by parsing JSON. Data MUST have at least
 * "name" and "id" as parameters for it to be displayed here.
 *
 * @param onChange callback function when an option is selected
 * @param options different options to select from
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Select(
    onChange: ((String) -> Unit)? = null,
    options: List<Map<String, String>> = listOf()
    ) {

    val space = LocalSpacing.current

    var mExpanded by remember { mutableStateOf(false) }
    var mSelectedText by remember { mutableStateOf("") }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(Modifier.padding(bottom = space.m, top = space.xs)) {
        ExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxWidth(),
            expanded = mExpanded,
            onExpandedChange = { mExpanded = !mExpanded }
        ) {
            OutlinedTextField(
                value = mSelectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = mExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = mExpanded,
                onDismissRequest = { mExpanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option["name"] ?: "") },
                        onClick = {
                            mSelectedText = option["name"] ?: ""
                            onChange?.invoke(option["id"] ?: "")
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