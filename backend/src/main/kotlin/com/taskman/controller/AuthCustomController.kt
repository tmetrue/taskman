package com.taskman.controller

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.rules.SecurityRule
import io.micronaut.security.token.render.BearerAccessRefreshToken
import jakarta.validation.Valid
import org.slf4j.LoggerFactory

/**
 * Controller that provides authentication endpoints
 */
@Controller("/api/auth")
class AuthCustomController {
    private val logger = LoggerFactory.getLogger(AuthCustomController::class.java)

    @Post("/login")
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Produces(MediaType.APPLICATION_JSON)
    fun login(@Body @Valid credentials: UsernamePasswordCredentials, httpRequest: HttpRequest<*>): HttpResponse<BearerAccessRefreshToken> {
        // This will be handled by Micronaut Security
        return HttpResponse.ok()
    }
    
    @Post("/logout")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    fun logout(authentication: Authentication): HttpResponse<Map<String, String>> {
        logger.info("User '{}' logged out", authentication.name)
        return HttpResponse.ok(mapOf("message" to "Logged out successfully"))
    }
    
    @Get("/me")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    fun getCurrentUser(authentication: Authentication): HttpResponse<Map<String, Any>> {
        val userData = mapOf(
            "id" to (authentication.attributes["id"] ?: ""),
            "username" to authentication.name,
            "email" to (authentication.attributes["email"] ?: ""),
            "firstName" to (authentication.attributes["firstName"] ?: ""),
            "lastName" to (authentication.attributes["lastName"] ?: ""),
            "role" to (authentication.attributes["role"] ?: ""),
            "roles" to authentication.roles
        )
        
        return HttpResponse.ok(userData)
    }
    
    @Get("/check-token")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun checkToken(httpRequest: HttpRequest<*>): HttpResponse<Map<String, Any>> {
        val authHeader = httpRequest.headers.get("Authorization") ?: ""
        
        if (!authHeader.startsWith("Bearer ")) {
            return HttpResponse.ok(mapOf(
                "valid" to false,
                "message" to "No bearer token provided"
            ))
        }
        
        // Since this endpoint itself requires authentication, 
        // if we've reached this point, the token is valid
        return HttpResponse.ok(mapOf("valid" to true))
    }
}