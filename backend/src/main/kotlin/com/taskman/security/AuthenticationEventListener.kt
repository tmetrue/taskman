package com.taskman.security

import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.event.LoginFailedEvent
import io.micronaut.security.event.LoginSuccessfulEvent
import io.micronaut.security.event.LogoutEvent
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

/**
 * Listens for authentication events and logs them appropriately
 */
@Singleton
class AuthenticationEventListener : ApplicationEventListener<LoginFailedEvent> {
    
    private val logger = LoggerFactory.getLogger(AuthenticationEventListener::class.java)
    
    override fun onApplicationEvent(event: LoginFailedEvent) {
        val username = event.source.toString().substringAfter("identity='").substringBefore("'") 
        logger.warn("Login failed for user '{}' - {}", username, event.source.toString())
    }
}

/**
 * Listener for successful login events
 */
@Singleton
class LoginSuccessListener : ApplicationEventListener<LoginSuccessfulEvent> {
    
    private val logger = LoggerFactory.getLogger(LoginSuccessListener::class.java)
    
    override fun onApplicationEvent(event: LoginSuccessfulEvent) {
        val authentication = event.source as Authentication
        val username = authentication.name
        val roles = authentication.roles.joinToString()
        
        logger.info("User '{}' logged in successfully with roles: {}", username, roles)
    }
}

/**
 * Listener for logout events
 */
@Singleton
class LogoutListener : ApplicationEventListener<LogoutEvent> {
    
    private val logger = LoggerFactory.getLogger(LogoutListener::class.java)
    
    override fun onApplicationEvent(event: LogoutEvent) {
        val authentication = event.source as Authentication
        val username = authentication.name
        logger.info("User '{}' logged out", username)
    }
}