package com.securityLearningProject.securityLearningProject.config

import com.securityLearningProject.securityLearningProject.service.`interface`.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig {

    @Autowired
    private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    @Autowired
    private lateinit var userService: UserService

    // UserDetailsService configs the service for fetching users on the database
    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService {
                username: String -> userService.findByUsername(username)
        }
    }

    // PassordEncoder is the config of encoding the password
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    // AuthenticationProvider configs which of the others configs to use, here setting passencoder and service
    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val daoAuthenticationProvider = DaoAuthenticationProvider()
        daoAuthenticationProvider.setUserDetailsService(userDetailsService())
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder())
        return daoAuthenticationProvider
    }

    // AuthenticationManager is the config for the authenticate config path
    @Bean
    fun publicAuthenticationManager(config: AuthenticationConfiguration?): AuthenticationManager {
        if (config != null) {
            return config.authenticationManager
        } else throw RuntimeException()
    }

    // Configuration for segurity, regarding how and when spring will check and act on requests
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        // certificate of security request form: verifies that the sender of request is the client of the api,
        // used when api is consumed by a front, not usefull when api is only consumed by others api.
        http
            .csrf()
            .disable()
        // configuration of permissions and authentications for every available path
        http
            .authorizeRequests()
            .antMatchers(HttpMethod.POST,"/", "/api/v1/auth/getToken", "/api/v1/auth/register", "/api/v1/auth/login")
            .permitAll()
            .anyRequest()
            .authenticated()
        // declaration of stateless auth, since using token generated and not persisted
        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        // config of order of filters, using my custom filter for this application
        http
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        // return this config
        return http.build()
    }
}
