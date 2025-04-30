package com.taskman.dto

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class AuthRequest(
    val username: String,
    val password: String
)