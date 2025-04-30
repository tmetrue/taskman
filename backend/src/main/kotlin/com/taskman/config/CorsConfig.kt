package com.taskman.config

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.annotation.RequestAttribute

@ConfigurationProperties("cors")
@Requires(property = "cors.enabled", value = "true")
class CorsConfig {
    var enabled: Boolean = true
    var allowedOrigins: List<String> = listOf("http://localhost:5174")
    var allowedMethods: List<String> = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
    var allowedHeaders: List<String> = listOf("Authorization", "Content-Type")
    var exposedHeaders: List<String> = listOf("Authorization")
    var allowCredentials: Boolean = true
    var maxAge: Long = 3600
} 