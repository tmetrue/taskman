package com.taskman.filter

import io.micronaut.http.HttpAttributes
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux

/**
 * Filter to log security-related HTTP responses, especially 401 and 403 errors
 */
@Filter("/api/**") 
class SecurityLoggingFilter : HttpServerFilter {
    
    private val logger = LoggerFactory.getLogger(SecurityLoggingFilter::class.java)
    
    override fun doFilter(request: HttpRequest<*>, chain: ServerFilterChain): Publisher<MutableHttpResponse<*>> {
        return Flux.from(chain.proceed(request))
            .doOnNext { response -> 
                // Log unauthorized and forbidden responses
                when (response.status) {
                    HttpStatus.UNAUTHORIZED -> logSecurityFailure(request, response, "Unauthorized")
                    HttpStatus.FORBIDDEN -> logSecurityFailure(request, response, "Forbidden")
                    else -> { /* Do nothing for other statuses */ }
                }
            }
    }
    
    private fun logSecurityFailure(request: HttpRequest<*>, response: HttpResponse<*>, reason: String) {
        val path = request.path
        val method = request.method
        val principal = request.attributes.get(HttpAttributes.PRINCIPAL, java.security.Principal::class.java)
        val username = if (principal != null) principal.toString() else "anonymous"
        val ip = request.remoteAddress.address.hostAddress ?: "unknown"
        
        logger.warn("Security failure: {} - User: '{}', Method: {}, Path: {}, Source IP: {}", 
            reason, username, method, path, ip)
    }
}