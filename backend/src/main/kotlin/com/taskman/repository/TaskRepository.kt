package com.taskman.repository

import com.taskman.model.Task
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.POSTGRES)
interface TaskRepository : CrudRepository<Task, Long> {
    fun findByCompleted(completed: Boolean): List<Task>
}