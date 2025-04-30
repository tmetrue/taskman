package com.taskman

import com.taskman.model.Task
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Basic tests for the Task model
 */
class TaskControllerTest {

    @Test
    fun testTaskModel() {
        // Create a sample task
        val task = Task(
            id = 1L,
            title = "Test Task",
            description = "Test Description",
            completed = false,
            userId = 100L
        )
        
        // Verify task properties
        assertEquals(1L, task.id)
        assertEquals("Test Task", task.title)
        assertEquals("Test Description", task.description)
        assertEquals(false, task.completed)
        assertEquals(100L, task.userId)
    }
}