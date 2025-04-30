package com.taskman.controller

import com.taskman.dto.AuthRequest
import com.taskman.dto.RegisterRequest
import com.taskman.dto.UserResponse
import com.taskman.service.UserService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import jakarta.validation.Valid

@Validated
@Controller("/api/auth")
@Secured(SecurityRule.IS_ANONYMOUS)
class AuthController(@Inject private val userService: UserService) {

    @Post("/register")
    fun register(@Body @Valid request: RegisterRequest): HttpResponse<UserResponse> {
        try {
            val user = userService.register(request)
            return HttpResponse.created(user)
        } catch (e: IllegalArgumentException) {
            return HttpResponse.badRequest(UserResponse(null, "", "", null, null))
        }
    }
    
    // Login is handled by Micronaut Security JWT authentication
}