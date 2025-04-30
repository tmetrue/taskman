package com.taskman.service

import com.taskman.model.Task
import com.taskman.repository.TaskRepository
import jakarta.inject.Singleton

@Singleton
class TaskService(private val taskRepository: TaskRepository) {

    fun getAllTasks(): List<Task> {
        return taskRepository.findAll().toList()
    }
    
    fun getTasksForUser(userId: Long): List<Task> {
        return taskRepository.findByUserId(userId)
    }
    
    fun getTasksForUserByCategory(userId: Long, categoryId: Long): List<Task> {
        return taskRepository.findByUserIdAndCategoryId(userId, categoryId)
    }
    
    fun getTasksByCategory(categoryId: Long): List<Task> {
        return taskRepository.findByCategoryId(categoryId)
    }

    fun getTaskById(id: Long): Task? {
        return taskRepository.findById(id).orElse(null)
    }

    fun createTask(task: Task): Task {
        return taskRepository.save(task)
    }

    fun updateTask(id: Long, task: Task): Task? {
        if (!taskRepository.existsById(id)) {
            return null
        }
        
        // Keep the original task ID
        task.id = id
        
        // Get the existing task to verify ownership
        val existingTask = taskRepository.findById(id).orElse(null) ?: return null
        
        // Preserve the user ID from the existing task
        task.userId = existingTask.userId
        
        return taskRepository.update(task)
    }

    fun deleteTask(id: Long, userId: Long): Boolean {
        val task = taskRepository.findById(id).orElse(null) ?: return false
        
        // Verify the user owns this task
        if (task.userId != userId) {
            return false
        }
        
        taskRepository.deleteById(id)
        return true
    }
    
    fun findTasksByCompleted(completed: Boolean): List<Task> {
        return taskRepository.findByCompleted(completed)
    }
    
    fun findUserTasksByCompleted(userId: Long, completed: Boolean): List<Task> {
        return taskRepository.findByUserIdAndCompleted(userId, completed)
    }
}