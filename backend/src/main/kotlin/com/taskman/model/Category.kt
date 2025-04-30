package com.taskman.model

import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable
import java.time.Instant

@Serdeable
@MappedEntity(value = "categories")
data class Category(
    @field:Id
    @field:GeneratedValue
    var id: Long? = null,
    
    var name: String,
    var description: String? = null,
    var displayOrder: Int = 0,
    
    @DateCreated
    var createdAt: Instant? = null,
    
    @DateUpdated
    var updatedAt: Instant? = null
)