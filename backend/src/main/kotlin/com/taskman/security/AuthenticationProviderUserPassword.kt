package com.taskman.security

import com.taskman.service.UserService
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationFailed
import io.micronaut.security.authentication.AuthenticationFailureReason
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink

@Singleton
class AuthenticationProviderUserPassword(private val userService: UserService) : AuthenticationProvider {
    
    override fun authenticate(
        httpRequest: HttpRequest<*>?,
        authenticationRequest: AuthenticationRequest<*, *>
    ): Publisher<AuthenticationResponse> {
        return Flux.create({ emitter: FluxSink<AuthenticationResponse> ->
            val username = authenticationRequest.identity.toString()
            val password = authenticationRequest.secret.toString()
            
            val userOptional = userService.findByUsername(username)
            
            if (userOptional.isEmpty) {
                emitter.next(AuthenticationFailed(AuthenticationFailureReason.USER_NOT_FOUND))
                emitter.complete()
                return@create
            }
            
            val user = userOptional.get()
            
            if (!user.enabled) {
                emitter.next(AuthenticationFailed(AuthenticationFailureReason.USER_DISABLED))
                emitter.complete()
                return@create
            }
            
            if (!userService.verifyPassword(user, password)) {
                emitter.next(AuthenticationFailed(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH))
                emitter.complete()
                return@create
            }
            
            // Authentication successful
            emitter.next(
                AuthenticationResponse.success(
                    user.username,
                    listOf("ROLE_USER"),
                    mapOf(
                        "id" to user.id,
                        "email" to user.email,
                        "firstName" to (user.firstName ?: ""),
                        "lastName" to (user.lastName ?: "")
                    )
                )
            )
            emitter.complete()
        }, FluxSink.OverflowStrategy.ERROR)
    }
}