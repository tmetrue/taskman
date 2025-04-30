package com.taskman.controller

import com.taskman.model.Task
import com.taskman.service.TaskService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.annotation.Controller
import jakarta.inject.Inject

@Controller("/api/tasks")
class TaskController(@Inject private val taskService: TaskService) {

    @Get
    fun getAllTasks(): HttpResponse<List<Task>> {
        return HttpResponse.ok(taskService.getAllTasks())
    }

    @Get("/{id}")
    fun getTaskById(id: Long): HttpResponse<Task> {
        return taskService.getTaskById(id)?.let {
            HttpResponse.ok(it)
        } ?: HttpResponse.notFound()
    }
    
    @Get("/status/{completed}")
    fun getTasksByStatus(completed: Boolean): HttpResponse<List<Task>> {
        return HttpResponse.ok(taskService.findTasksByCompleted(completed))
    }

    @Post
    fun createTask(@Body task: Task): HttpResponse<Task> {
        return HttpResponse.created(taskService.createTask(task))
    }

    @Put("/{id}")
    fun updateTask(id: Long, @Body task: Task): HttpResponse<Task> {
        return taskService.updateTask(id, task)?.let {
            HttpResponse.ok(it)
        } ?: HttpResponse.notFound()
    }

    @Delete("/{id}")
    fun deleteTask(id: Long): HttpResponse<Unit> {
        return if (taskService.deleteTask(id)) {
            HttpResponse.noContent()
        } else {
            HttpResponse.notFound()
        }
    }
}