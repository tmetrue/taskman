package com.taskman.service

import com.taskman.model.Task
import jakarta.inject.Singleton
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Singleton
class TaskService {
    private val tasks = ConcurrentHashMap<Long, Task>()
    private val idGenerator = AtomicLong(1)

    fun getAllTasks(): List<Task> {
        return tasks.values.toList()
    }

    fun getTaskById(id: Long): Task? {
        return tasks[id]
    }

    fun createTask(task: Task): Task {
        val id = idGenerator.getAndIncrement()
        task.id = id
        tasks[id] = task
        return task
    }

    fun updateTask(id: Long, task: Task): Task? {
        if (!tasks.containsKey(id)) {
            return null
        }
        task.id = id
        tasks[id] = task
        return task
    }

    fun deleteTask(id: Long): Boolean {
        return tasks.remove(id) != null
    }
}