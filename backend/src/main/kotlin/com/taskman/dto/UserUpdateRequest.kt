package com.taskman.dto

import com.taskman.model.Role
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

@Serdeable
data class UserUpdateRequest(
    @field:Size(min = 3, max = 50)
    val username: String? = null,
    
    @field:Email
    val email: String? = null,
    
    @field:Size(min = 6, max = 100)
    val password: String? = null,
    
    val firstName: String? = null,
    val lastName: String? = null,
    val role: Role? = null,
    val enabled: Boolean? = null
)