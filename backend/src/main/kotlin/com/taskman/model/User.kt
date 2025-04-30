package com.taskman.model

import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation
import io.micronaut.security.annotation.Secured
import io.micronaut.serde.annotation.Serdeable
import java.time.Instant

@Serdeable
@MappedEntity(value = "users")
data class User(
    @field:Id
    @field:GeneratedValue
    var id: Long? = null,
    
    var username: String,
    var email: String,
    var passwordHash: String,
    var firstName: String? = null,
    var lastName: String? = null,
    var enabled: Boolean = true,
    
    @DateCreated
    var createdAt: Instant? = null,
    
    @DateUpdated
    var updatedAt: Instant? = null
) {
    // Don't include password hash in toString for security
    override fun toString(): String =
        "User(id=$id, username=$username, email=$email, firstName=$firstName, lastName=$lastName, enabled=$enabled)"
}