package com.taskman.controller

import com.taskman.dto.RegisterRequest
import com.taskman.dto.UserResponse
import com.taskman.dto.UserUpdateRequest
import com.taskman.model.Role
import com.taskman.service.UserService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import jakarta.validation.Valid

@Validated
@Controller("/api/admin/users")
@Secured("ROLE_ADMIN")  // Only admins can access this controller
class AdminController(@Inject private val userService: UserService) {

    @Get
    fun getAllUsers(): HttpResponse<List<UserResponse>> {
        val users = userService.getAllUsers()
        return HttpResponse.ok(users.map { UserResponse.from(it) })
    }
    
    @Get("/{id}")
    fun getUserById(id: Long): HttpResponse<UserResponse> {
        val userOptional = userService.findById(id)
        if (userOptional.isEmpty) {
            return HttpResponse.notFound()
        }
        
        return HttpResponse.ok(UserResponse.from(userOptional.get()))
    }
    
    @Post
    fun createUser(@Body @Valid request: RegisterRequest, @QueryValue(defaultValue = "USER") role: Role): HttpResponse<UserResponse> {
        try {
            val user = userService.createUser(request, role)
            return HttpResponse.created(user)
        } catch (e: IllegalArgumentException) {
            return HttpResponse.badRequest()
        }
    }
    
    @Put("/{id}")
    fun updateUser(id: Long, @Body request: UserUpdateRequest): HttpResponse<UserResponse> {
        try {
            val updatedUser = userService.updateUser(id, request)
            return if (updatedUser != null) {
                HttpResponse.ok(updatedUser)
            } else {
                HttpResponse.notFound()
            }
        } catch (e: IllegalArgumentException) {
            return HttpResponse.badRequest()
        }
    }
    
    @Delete("/{id}")
    fun deleteUser(id: Long): HttpResponse<Unit> {
        // Prevent deleting the admin user itself
        val userOptional = userService.findById(id)
        if (userOptional.isEmpty) {
            return HttpResponse.notFound()
        }
        
        val user = userOptional.get()
        if (user.username == "admin") {
            return HttpResponse.badRequest()
        }
        
        return if (userService.deleteUser(id)) {
            HttpResponse.noContent()
        } else {
            HttpResponse.notFound()
        }
    }
}