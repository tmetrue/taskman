package com.taskman.repository

import com.taskman.model.Task
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.POSTGRES)
interface TaskRepository : CrudRepository<Task, Long> {
    fun findByCompleted(completed: Boolean): List<Task>
    fun findByUserId(userId: Long): List<Task>
    fun findByUserIdAndCompleted(userId: Long, completed: Boolean): List<Task>
    fun findByUserIdAndCategoryId(userId: Long, categoryId: Long): List<Task>
    fun findByCategoryId(categoryId: Long): List<Task>
}