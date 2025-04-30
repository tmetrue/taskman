package com.taskman.dto

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Serdeable
data class CategoryRequest(
    @field:NotBlank
    @field:Size(min = 1, max = 50)
    val name: String,
    
    val description: String? = null,
    val displayOrder: Int = 0
)