package com.taskman

import com.taskman.model.Task
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@MicronautTest
class TaskControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun testCreateAndGetTask() {
        // Create a task
        val task = Task(
            title = "Test Task",
            description = "Test Description",
            completed = false
        )
        
        val request = HttpRequest.POST("/api/tasks", task)
        val createdTask = client.toBlocking().exchange(request, Task::class.java).body()
        
        assertNotNull(createdTask.id)
        assertEquals("Test Task", createdTask.title)
        
        // Get the created task
        val getRequest = HttpRequest.GET<Any>("/api/tasks/${createdTask.id}")
        val retrievedTask = client.toBlocking().exchange(getRequest, Task::class.java).body()
        
        assertEquals(createdTask.id, retrievedTask.id)
        assertEquals("Test Task", retrievedTask.title)
    }
}