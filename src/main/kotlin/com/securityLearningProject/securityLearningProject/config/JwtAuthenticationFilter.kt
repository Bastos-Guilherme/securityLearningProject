package com.securityLearningProject.securityLearningProject.config

import com.securityLearningProject.securityLearningProject.model.User
import com.securityLearningProject.securityLearningProject.service.`interface`.JwtService
import com.securityLearningProject.securityLearningProject.service.`interface`.AuthService
import com.securityLearningProject.securityLearningProject.service.`interface`.UserService
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

// This is a filter to be used with all requests in this application
@Component
class JwtAuthenticationFilter: OncePerRequestFilter() {

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var userService: UserService

    // implements OncePerRequestFilter, and this only has one fun
    override fun doFilterInternal(
        @NotNull request: HttpServletRequest,
        @NotNull response: HttpServletResponse,
        @NotNull filterChain: FilterChain
    ) {
        // gets the token from the request
        val authHeader: String = request.getHeader("Authorization").orEmpty()
        // checks if the token is Bearer and existent
        if (!authHeader.startsWith("Bearer ")) {
            // do nothing if not and exits the filter
            filterChain.doFilter(request, response)
            return
        }
        // then gets the token without the Bearer
        val jwt: String = authHeader.substring(7)
        // extract the username from the token
        val username: String = jwtService.extractUsername(jwt)
        // checks if the authentication was not already done
        if (SecurityContextHolder.getContext().authentication == null) {
            // find the user in the DB with this username
            val user: User = userService.findByUsername(username)
            // check if this is a valid token for this user
            if (jwtService.isTokenValid(jwt, user)) {
                // validade the account situated
                if (authService.validadeUser(user.username, user.password)) {
                    // and validade the auth to pass along the request
                    val authToken = UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        }
        // call next filter, with or without the auth
        filterChain.doFilter(request, response)
    }
}
