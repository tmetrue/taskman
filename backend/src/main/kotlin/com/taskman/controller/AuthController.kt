package com.taskman.controller

// import com.taskman.dto.AuthRequest - not used
import com.taskman.dto.RegisterRequest
import com.taskman.dto.UserResponse
import com.taskman.model.Role
import com.taskman.service.UserService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import jakarta.validation.Valid

@Validated
@Controller("/api/auth")
class AuthController(@Inject private val userService: UserService) {

    @Post("/register")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun register(@Body @Valid request: RegisterRequest): HttpResponse<UserResponse> {
        try {
            val user = userService.register(request)
            return HttpResponse.created(user)
        } catch (e: IllegalArgumentException) {
            return HttpResponse.badRequest()
        }
    }
    
    @Get("/me")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    fun getCurrentUser(authentication: Authentication): HttpResponse<UserResponse> {
        val username = authentication.name
        val userOptional = userService.findByUsername(username)
        
        if (userOptional.isEmpty) {
            return HttpResponse.notFound()
        }
        
        return HttpResponse.ok(UserResponse.from(userOptional.get()))
    }
    
    // Login is handled by Micronaut Security JWT authentication
}