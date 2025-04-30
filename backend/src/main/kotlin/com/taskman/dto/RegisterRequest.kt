package com.taskman.dto

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Serdeable
data class RegisterRequest(
    @field:NotBlank
    @field:Size(min = 3, max = 50)
    val username: String,
    
    @field:NotBlank
    @field:Email
    val email: String,
    
    @field:NotBlank
    @field:Size(min = 6, max = 100)
    val password: String,
    
    val firstName: String? = null,
    val lastName: String? = null
)