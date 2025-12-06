package com.example.studygroup

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import components.TextInput
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.compose.ui.platform.LocalFocusManager
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class TextInputTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    @Test
    fun displaysLabel() {
        composeTestRule.setContent {
            TextInput(value = "", label = "Username")
        }

        composeTestRule.onNodeWithText("Username").assertIsDisplayed()
    }

    @Test
    fun textChangesOnInput() {
        var updatedText = ""

        composeTestRule.setContent {
            TextInput(
                value = updatedText,
                onChange = { updatedText = it },
                label = "Input"
            )
        }

        composeTestRule.onNode(hasSetTextAction())
            .performTextInput("Hello")

        assert(updatedText == "Hello")
    }

    @Test
    fun focus_callsOnFocusAndBlur() {
        var focused = false
        var blurred = false


        composeTestRule.setContent {
            Column {
                TextInput(
                    value = "",
                    onFocus = { focused = true },
                    onBlur = { blurred = true }
                )


                // A focusable element to take focus away
                val focusManager = LocalFocusManager.current
                Button(
                    onClick = { focusManager.clearFocus() },
                    modifier = Modifier.testTag("otherField").focusable()
                ) {
                    Text("test button")
                }
            }
        }


        val input = composeTestRule.onNode(hasSetTextAction())
        val other = composeTestRule.onNodeWithTag("otherField")

        // Gain focus
        input.performClick()
        assert(focused)

        // Lose focus by moving focus to another field
        other.performClick()
        assert(blurred)
    }
}
