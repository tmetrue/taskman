package com.taskman.controller

import com.taskman.model.Task
import com.taskman.service.CategoryService
import com.taskman.service.TaskService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.annotation.Controller
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import jakarta.inject.Inject

@Controller("/api/tasks")
@Secured(SecurityRule.IS_AUTHENTICATED)
class TaskController(
    @Inject private val taskService: TaskService,
    @Inject private val categoryService: CategoryService
) {

    @Get
    @Secured("ROLE_ADMIN")  // Admin only can see all tasks
    fun getAllTasks(): HttpResponse<List<Task>> {
        return HttpResponse.ok(taskService.getAllTasks())
    }
    
    @Get("/my")
    fun getMyTasks(authentication: Authentication): HttpResponse<List<Task>> {
        val userId = authentication.attributes["id"]?.toString()?.toLong() 
            ?: return HttpResponse.unauthorized()
        
        return HttpResponse.ok(taskService.getTasksForUser(userId))
    }

    @Get("/{id}")
    fun getTaskById(id: Long, authentication: Authentication): HttpResponse<Task> {
        val task = taskService.getTaskById(id) ?: return HttpResponse.notFound()
        
        // Check if this task belongs to the current user or is admin
        val userId = authentication.attributes["id"]?.toString()?.toLong()
        val isAdmin = authentication.roles.contains("ROLE_ADMIN")
        
        if (!isAdmin && userId != null && task.userId != userId) {
            return HttpResponse.unauthorized()
        }
        
        return HttpResponse.ok(task)
    }
    
    @Get("/status/{completed}")
    fun getTasksByStatus(completed: Boolean, authentication: Authentication): HttpResponse<List<Task>> {
        val userId = authentication.attributes["id"]?.toString()?.toLong() 
            ?: return HttpResponse.unauthorized()
        
        return HttpResponse.ok(taskService.findUserTasksByCompleted(userId, completed))
    }
    
    @Get("/category/{categoryId}")
    fun getTasksByCategory(categoryId: Long, authentication: Authentication): HttpResponse<List<Task>> {
        val userId = authentication.attributes["id"]?.toString()?.toLong() 
            ?: return HttpResponse.unauthorized()
        val isAdmin = authentication.roles.contains("ROLE_ADMIN")
        
        // Verify category exists
        val categoryExists = categoryService.getCategoryById(categoryId).isPresent
        if (!categoryExists) {
            return HttpResponse.notFound()
        }
        
        val tasks = if (isAdmin) {
            // Admins can see all tasks in a category
            taskService.getTasksByCategory(categoryId)
        } else {
            // Regular users can only see their own tasks in a category
            taskService.getTasksForUserByCategory(userId, categoryId)
        }
        
        return HttpResponse.ok(tasks)
    }

    @Post
    fun createTask(@Body task: Task, authentication: Authentication): HttpResponse<Task> {
        val userId = authentication.attributes["id"]?.toString()?.toLong() 
            ?: return HttpResponse.unauthorized()
        
        // Set the owner of the task
        task.userId = userId
        
        // Verify category exists if provided
        if (task.categoryId != null) {
            val categoryExists = categoryService.getCategoryById(task.categoryId!!).isPresent
            if (!categoryExists) {
                return HttpResponse.badRequest()
            }
        }
        
        return HttpResponse.created(taskService.createTask(task))
    }

    @Put("/{id}")
    fun updateTask(id: Long, @Body task: Task, authentication: Authentication): HttpResponse<Task> {
        val userId = authentication.attributes["id"]?.toString()?.toLong() 
            ?: return HttpResponse.unauthorized()
        val isAdmin = authentication.roles.contains("ROLE_ADMIN")
        
        // Get existing task to verify ownership
        val existingTask = taskService.getTaskById(id) ?: return HttpResponse.notFound()
        
        // Verify ownership (unless admin)
        if (!isAdmin && existingTask.userId != userId) {
            return HttpResponse.unauthorized()
        }
        
        // Verify category exists if provided
        if (task.categoryId != null) {
            val categoryExists = categoryService.getCategoryById(task.categoryId!!).isPresent
            if (!categoryExists) {
                return HttpResponse.badRequest()
            }
        }
        
        return taskService.updateTask(id, task)?.let {
            HttpResponse.ok(it)
        } ?: HttpResponse.notFound()
    }

    @Delete("/{id}")
    fun deleteTask(id: Long, authentication: Authentication): HttpResponse<Unit> {
        val userId = authentication.attributes["id"]?.toString()?.toLong() 
            ?: return HttpResponse.unauthorized()
        val isAdmin = authentication.roles.contains("ROLE_ADMIN")
        
        // Verify ownership if not admin
        if (!isAdmin) {
            return if (taskService.deleteTask(id, userId)) {
                HttpResponse.noContent()
            } else {
                HttpResponse.notFound()
            }
        }
        
        // Admins can delete any task
        val existingTask = taskService.getTaskById(id) ?: return HttpResponse.notFound()
        taskService.deleteTask(id, existingTask.userId ?: 0)
        return HttpResponse.noContent()
    }
}