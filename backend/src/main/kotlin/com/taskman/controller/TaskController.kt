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
class TaskController(
    @Inject private val taskService: TaskService,
    @Inject private val categoryService: CategoryService
) {

    @Get
    @Secured(SecurityRule.IS_ANONYMOUS)  // Allow anonymous access
    fun getAllTasks(): HttpResponse<List<Task>> {
        return HttpResponse.ok(taskService.getAllTasks())
    }
    
    @Get("/my")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    fun getMyTasks(authentication: Authentication): HttpResponse<List<Task>> {
        val userId = authentication.attributes["id"]?.toString()?.toLong() 
            ?: return HttpResponse.unauthorized()
        
        return HttpResponse.ok(taskService.getTasksForUser(userId))
    }

    @Get("/{id}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun getTaskById(id: Long, authentication: Authentication?): HttpResponse<Task> {
        val task = taskService.getTaskById(id) ?: return HttpResponse.notFound()
        
        // If user is authenticated, check if they have permission to view this task
        if (authentication != null) {
            val userId = authentication.attributes["id"]?.toString()?.toLong()
            val isAdmin = authentication.roles.contains("ROLE_ADMIN")
            
            // If it's a private task and user is not admin or owner, deny access
            if (task.userId != null && !isAdmin && userId != task.userId) {
                return HttpResponse.unauthorized()
            }
        }
        
        return HttpResponse.ok(task)
    }
    
    @Get("/status/{completed}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun getTasksByStatus(completed: Boolean): HttpResponse<List<Task>> {
        return HttpResponse.ok(taskService.findTasksByCompleted(completed))
    }
    
    @Get("/category/{categoryId}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun getTasksByCategory(categoryId: Long): HttpResponse<List<Task>> {
        // Verify category exists
        val categoryExists = categoryService.getCategoryById(categoryId).isPresent
        if (!categoryExists) {
            return HttpResponse.notFound()
        }
        
        // Return all tasks in the category (no filtering)
        return HttpResponse.ok(taskService.getTasksByCategory(categoryId))
    }

    @Post
    @Secured(SecurityRule.IS_AUTHENTICATED)
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
    @Secured(SecurityRule.IS_AUTHENTICATED)
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
    @Secured(SecurityRule.IS_AUTHENTICATED)
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