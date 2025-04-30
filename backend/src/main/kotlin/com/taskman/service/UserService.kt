package com.taskman.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.taskman.dto.RegisterRequest
import com.taskman.dto.UserResponse
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
            lastName = request.lastName
        )
        
        // Save the user
        val savedUser = userRepository.save(user)
        
        // Return user response (without password)
        return UserResponse.from(savedUser)
    }
    
    fun verifyPassword(user: User, password: String): Boolean {
        val result = BCrypt.verifyer().verify(
            password.toCharArray(),
            user.passwordHash
        )
        return result.verified
    }
}