package com.taskman.dto

import com.taskman.model.Role
import com.taskman.model.User
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class UserResponse(
    val id: Long?,
    val username: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val role: Role,
    val enabled: Boolean
) {
    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                id = user.id,
                username = user.username,
                email = user.email,
                firstName = user.firstName,
                lastName = user.lastName,
                role = user.role,
                enabled = user.enabled
            )
        }
    }
}