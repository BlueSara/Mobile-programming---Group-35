package com.example.studygroup

import org.junit.Assert.*
import org.junit.Test
import viewModel.addNewSubject


/**
 * Unit tests for "addNewSubject" from "\..\viewModel\UserAccount.kt"
 */
class UserAccountUnitTest {

    @Test
    fun `returns false if subjectName is empty`() {
        val subject = mutableMapOf<String, Any>(
            "subjectName" to "",
            "subjectCode" to "PROG1001",
            "obligatory" to true
        )

        var successCalled = false

        val result = addNewSubject(subject) { successCalled = true }

        assertFalse(result)
        assertFalse(successCalled)
    }

    @Test
    fun `returns false if subjectCode is empty`() {
        val subject = mutableMapOf<String, Any>(
            "subjectName" to "Programming",
            "subjectCode" to "",
            "obligatory" to true
        )

        var successCalled = false

        val result = addNewSubject(subject) { successCalled = true }

        assertFalse(result)
        assertFalse(successCalled)
    }

    @Test
    fun `returns false if obligatory is null`() {
        val subject = mutableMapOf<String, Any>(
            "subjectName" to "Programming",
            "subjectCode" to "PROG1001"
        )

        var successCalled = false

        val result = addNewSubject(subject) { successCalled = true }

        assertFalse(result)
        assertFalse(successCalled)
    }

    @Test
    fun `sets partOfStudyProgram to true if obligatory is false and partOfStudyProgram is null`() {
        val subject = mutableMapOf<String, Any>(
            "subjectName" to "Programming",
            "subjectCode" to "PROG1001",
            "obligatory" to false
        )

        var successCalled = false

        val result = addNewSubject(subject) { successCalled = true }

        assertTrue(result)
        assertTrue(successCalled)
        assertTrue(subject.isEmpty()) // subject is cleared after success
    }

    @Test
    fun `calls onSuccess and clears map on valid input`() {
        val subject = mutableMapOf<String, Any>(
            "subjectName" to "Programming",
            "subjectCode" to "PROG1001",
            "obligatory" to true
        )

        var successCalled = false

        val result = addNewSubject(subject) { successCalled = true }

        assertTrue(result)
        assertTrue(successCalled)
        assertTrue(subject.isEmpty()) // cleared after success
    }
}
