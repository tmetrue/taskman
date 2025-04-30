package com.taskman.config

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import jakarta.inject.Inject

@Filter("/**")
@Requires(property = "cors.enabled", value = "true")
class CorsFilter @Inject constructor(private val corsConfig: CorsConfig) : HttpServerFilter {

    override fun doFilter(request: HttpRequest<*>, chain: ServerFilterChain): Publisher<MutableHttpResponse<*>> {
        return if (request.method == HttpMethod.OPTIONS) {
            // Handle preflight requests
            Flux.just(HttpResponse.ok<Any>()
                .header("Access-Control-Allow-Origin", corsConfig.allowedOrigins.joinToString(","))
                .header("Access-Control-Allow-Methods", corsConfig.allowedMethods.joinToString(","))
                .header("Access-Control-Allow-Headers", corsConfig.allowedHeaders.joinToString(","))
                .header("Access-Control-Expose-Headers", corsConfig.exposedHeaders.joinToString(","))
                .header("Access-Control-Allow-Credentials", corsConfig.allowCredentials.toString())
                .header("Access-Control-Max-Age", corsConfig.maxAge.toString()))
        } else {
            // Handle actual requests
            Flux.from(chain.proceed(request))
                .map { response ->
                    response.headers.apply {
                        add("Access-Control-Allow-Origin", corsConfig.allowedOrigins.joinToString(","))
                        add("Access-Control-Allow-Methods", corsConfig.allowedMethods.joinToString(","))
                        add("Access-Control-Allow-Headers", corsConfig.allowedHeaders.joinToString(","))
                        add("Access-Control-Expose-Headers", corsConfig.exposedHeaders.joinToString(","))
                        add("Access-Control-Allow-Credentials", corsConfig.allowCredentials.toString())
                    }
                    response
                }
        }
    }
} 