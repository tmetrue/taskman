package com.taskman.service

import com.taskman.model.Task
import com.taskman.repository.TaskRepository
import jakarta.inject.Singleton

@Singleton
class TaskService(private val taskRepository: TaskRepository) {

    fun getAllTasks(): List<Task> {
        return taskRepository.findAll().toList()
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
        
        task.id = id
        return taskRepository.update(task)
    }

    fun deleteTask(id: Long): Boolean {
        if (!taskRepository.existsById(id)) {
            return false
        }
        
        taskRepository.deleteById(id)
        return true
    }
    
    fun findTasksByCompleted(completed: Boolean): List<Task> {
        return taskRepository.findByCompleted(completed)
    }
}