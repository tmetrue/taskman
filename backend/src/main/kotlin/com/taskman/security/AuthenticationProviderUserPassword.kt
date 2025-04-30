package com.taskman.security

import com.taskman.service.UserService
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

@Singleton
class AuthenticationProviderUserPassword(private val userService: UserService) : AuthenticationProvider<HttpRequest<*>> {
    
    private val logger = LoggerFactory.getLogger(AuthenticationProviderUserPassword::class.java)
    
    override fun authenticate(
        @Nullable httpRequest: HttpRequest<*>?,
        authenticationRequest: AuthenticationRequest<*, *>
    ): Publisher<AuthenticationResponse> {
        return Mono.create { emitter ->
            val username = authenticationRequest.identity.toString()
            val sourceIp = httpRequest?.remoteAddress?.address?.hostAddress ?: "unknown"
            
            logger.info("Authentication attempt for user '{}' from IP {}", username, sourceIp)
            
            val userOptional = userService.findByUsername(username)
            
            if (userOptional.isEmpty) {
                logger.warn("Authentication failed: User '{}' not found. Source IP: {}", username, sourceIp)
                emitter.error(AuthenticationResponse.exception("User not found"))
                return@create
            }
            
            val user = userOptional.get()
            
            if (!user.enabled) {
                logger.warn("Authentication failed: Account '{}' is disabled. Source IP: {}", username, sourceIp)
                emitter.error(AuthenticationResponse.exception("User account is disabled"))
                return@create
            }
            
            if (!userService.verifyPassword(user, password = authenticationRequest.secret.toString())) {
                logger.warn("Authentication failed: Invalid credentials for user '{}'. Source IP: {}", username, sourceIp)
                emitter.error(AuthenticationResponse.exception("Invalid credentials"))
                return@create
            }
            
            // Authentication successful
            val roles = if (user.isAdmin()) {
                listOf("ROLE_USER", "ROLE_ADMIN")
            } else {
                listOf("ROLE_USER")
            }
            
            logger.info("Authentication successful for user '{}' with roles {}. Source IP: {}", 
                username, roles.joinToString(), sourceIp)
            
            emitter.success(
                AuthenticationResponse.success(
                    user.username,
                    roles,
                    mapOf(
                        "id" to user.id,
                        "email" to user.email,
                        "firstName" to (user.firstName ?: ""),
                        "lastName" to (user.lastName ?: ""),
                        "role" to user.role.toString()
                    )
                )
            )
        }
    }
}