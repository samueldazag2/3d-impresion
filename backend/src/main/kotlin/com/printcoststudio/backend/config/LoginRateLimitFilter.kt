package com.printcoststudio.backend.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

/**
 * In-memory sliding-window limiter for brute-force protection on login.
 * Per-instance only: fine for the single-instance Fase 1 deployment, but a
 * shared store (e.g. Redis) would be needed if the backend scales out to
 * multiple instances behind a load balancer.
 */
class LoginRateLimitFilter(
    private val maxAttempts: Int = 5,
    private val windowSeconds: Long = 60,
) : OncePerRequestFilter() {
    private val attempts = ConcurrentHashMap<String, MutableList<Instant>>()

    override fun shouldNotFilter(request: HttpServletRequest): Boolean = request.requestURI != LOGIN_PATH

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val key = request.remoteAddr
        val now = Instant.now()
        val windowStart = now.minusSeconds(windowSeconds)

        val recentAttempts =
            attempts.compute(key) { _, existing ->
                (existing ?: mutableListOf()).apply {
                    removeIf { it.isBefore(windowStart) }
                    add(now)
                }
            }!!

        if (recentAttempts.size > maxAttempts) {
            response.status = 429
            response.contentType = "application/json"
            response.writer.write("""{"detail":"Too many login attempts, try again later"}""")
            return
        }

        filterChain.doFilter(request, response)
    }

    companion object {
        private const val LOGIN_PATH = "/api/auth/login"
    }
}
