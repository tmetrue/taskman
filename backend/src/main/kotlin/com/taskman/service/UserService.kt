package com.taskman.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.taskman.dto.RegisterRequest
import com.taskman.dto.UserResponse
import com.taskman.dto.UserUpdateRequest
import com.taskman.model.Role
import com.taskman.model.User
import com.taskman.repository.UserRepository
import jakarta.inject.Singleton
import java.util.Optional

@Singleton
class UserService(private val userRepository: UserRepository) {

    fun findByUsername(username: String): Optional<User> {
        return userRepository.findByUsername(username)
    }
    
    fun findById(id: Long): Optional<User> {
        return userRepository.findById(id)
    }
    
    fun getAllUsers(): List<User> {
        return userRepository.findAll().toList()
    }
    
    fun register(request: RegisterRequest): UserResponse {
        // Check if username is already taken
        if (userRepository.existsByUsername(request.username)) {
            throw IllegalArgumentException("Username is already taken")
        }
        
        // Check if email is already in use
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email is already in use")
        }
        
        // Hash the password
        val passwordHash = BCrypt.withDefaults().hashToString(12, request.password.toCharArray())
        
        // Create the user
        val user = User(
            username = request.username,
            email = request.email,
            passwordHash = passwordHash,
            firstName = request.firstName,
            lastName = request.lastName,
            role = Role.USER  // Default role
        )
        
        // Save the user
        val savedUser = userRepository.save(user)
        
        // Return user response (without password)
        return UserResponse.from(savedUser)
    }
    
    fun createUser(request: RegisterRequest, role: Role = Role.USER): UserResponse {
        // Check if username is already taken
        if (userRepository.existsByUsername(request.username)) {
            throw IllegalArgumentException("Username is already taken")
        }
        
        // Check if email is already in use
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email is already in use")
        }
        
        // Hash the password
        val passwordHash = BCrypt.withDefaults().hashToString(12, request.password.toCharArray())
        
        // Create the user
        val user = User(
            username = request.username,
            email = request.email,
            passwordHash = passwordHash,
            firstName = request.firstName,
            lastName = request.lastName,
            role = role
        )
        
        // Save the user
        val savedUser = userRepository.save(user)
        
        // Return user response (without password)
        return UserResponse.from(savedUser)
    }
    
    fun updateUser(id: Long, request: UserUpdateRequest): UserResponse? {
        val existingUser = userRepository.findById(id).orElse(null) ?: return null
        
        // Check if username is already taken by another user
        if (request.username != null && 
            request.username != existingUser.username && 
            userRepository.existsByUsername(request.username)) {
            throw IllegalArgumentException("Username is already taken")
        }
        
        // Check if email is already in use by another user
        if (request.email != null && 
            request.email != existingUser.email && 
            userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email is already in use")
        }
        
        // Update fields if provided
        request.username?.let { existingUser.username = it }
        request.email?.let { existingUser.email = it }
        request.firstName?.let { existingUser.firstName = it }
        request.lastName?.let { existingUser.lastName = it }
        request.role?.let { existingUser.role = it }
        request.enabled?.let { existingUser.enabled = it }
        
        // Update password if provided
        request.password?.let {
            existingUser.passwordHash = BCrypt.withDefaults().hashToString(12, it.toCharArray())
        }
        
        // Save the updated user
        val updatedUser = userRepository.update(existingUser)
        
        // Return user response
        return UserResponse.from(updatedUser)
    }
    
    fun deleteUser(id: Long): Boolean {
        if (!userRepository.existsById(id)) {
            return false
        }
        
        userRepository.deleteById(id)
        return true
    }
    
    fun verifyPassword(user: User, password: String): Boolean {
        val result = BCrypt.verifyer().verify(
            password.toCharArray(),
            user.passwordHash
        )
        return result.verified
    }
}