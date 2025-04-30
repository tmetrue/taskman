package com.taskman.model

import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable
import java.time.Instant

@Serdeable
@MappedEntity(value = "tasks")
data class Task(
    @field:Id
    @field:GeneratedValue
    var id: Long? = null,
    
    var title: String,
    var description: String? = null,
    var completed: Boolean = false,
    var dueDate: String? = null,
    
    @DateCreated
    var createdAt: Instant? = null,
    
    @DateUpdated
    var updatedAt: Instant? = null
)