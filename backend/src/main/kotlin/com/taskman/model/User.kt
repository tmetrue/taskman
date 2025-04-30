package com.taskman.model

import com.fasterxml.jackson.annotation.JsonIgnore
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.security.annotation.Secured
import io.micronaut.serde.annotation.Serdeable
import java.time.Instant

enum class Role {
    USER, ADMIN
}

@Serdeable
@MappedEntity(value = "users")
data class User(
    @field:Id
    @field:GeneratedValue
    var id: Long? = null,
    
    var username: String,
    var email: String,
    
    @JsonIgnore
    var passwordHash: String,
    
    var firstName: String? = null,
    var lastName: String? = null,
    var role: Role = Role.USER,
    var enabled: Boolean = true,
    
    @DateCreated
    var createdAt: Instant? = null,
    
    @DateUpdated
    var updatedAt: Instant? = null
) {
    fun isAdmin(): Boolean = role == Role.ADMIN
    
    // Don't include password hash in toString for security
    override fun toString(): String =
        "User(id=$id, username=$username, email=$email, firstName=$firstName, lastName=$lastName, role=$role, enabled=$enabled)"
}