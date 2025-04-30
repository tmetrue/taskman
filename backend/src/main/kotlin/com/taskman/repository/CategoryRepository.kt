package com.taskman.repository

import com.taskman.model.Category
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.util.Optional

@JdbcRepository(dialect = Dialect.POSTGRES)
interface CategoryRepository : CrudRepository<Category, Long> {
    fun findByName(name: String): Optional<Category>
    fun existsByName(name: String): Boolean
    fun findAllOrderByDisplayOrderAsc(): List<Category>
}