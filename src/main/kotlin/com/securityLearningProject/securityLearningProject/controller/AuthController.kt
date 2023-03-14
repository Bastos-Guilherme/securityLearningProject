package com.securityLearningProject.securityLearningProject.controller

import com.securityLearningProject.securityLearningProject.controller.dto.RegistrationDTO
import com.securityLearningProject.securityLearningProject.controller.dto.TokenDTO
import com.securityLearningProject.securityLearningProject.model.Permission
import com.securityLearningProject.securityLearningProject.model.Role
import com.securityLearningProject.securityLearningProject.model.User
import com.securityLearningProject.securityLearningProject.service.`interface`.AuthService
import com.securityLearningProject.securityLearningProject.service.`interface`.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

// controller for the auth process
@RestController
@RequestMapping("api/v1/auth")
class AuthController() {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    // API endpoint responsible for an already registered user to get a token, unprotected
    @PostMapping("getToken")
    fun getToken(
        @RequestHeader("username", required = true) username: String,
        @RequestHeader("password", required = true) rawPassword: String
    ) : ResponseEntity<TokenDTO> {
        var password = passwordEncoder.encode(rawPassword)
        return ResponseEntity.ok(authService.getToken(username, password))
    }

    // API endpoint responsible for registration of a new user, unprotected
    @PostMapping("register")
    fun register(
        @RequestBody(required = true) registration: RegistrationDTO
    ) : ResponseEntity<Boolean> {
        val user = User()
        user.username = registration.username!!
        user.email = registration.email
        user.name = registration.name
        user.lastName = registration.lastName
        user.password = passwordEncoder.encode(registration.password)
        registration.roles?.forEach{
            user.roles += Role.valueOf(it)
        }
        registration.permissions?.forEach {
            user.permissions += Permission.valueOf(it)
        }
        userService.save(user)
        return ResponseEntity.ok(true)
    }

    // API endpoint responsible for "logout", invalidation of token, protected
    @PostMapping("logout")
    fun invalidateToken(
        @RequestHeader("Authorization", required = true) token: String
    ) : ResponseEntity<Boolean> {
        return ResponseEntity.ok(authService.inactivateUser(token))
    }

    // API endpoint responsible for login, activation of account, unprotected
    @PutMapping("login")
    fun activateUser(
        @RequestHeader("username", required = true) username: String,
        @RequestHeader("password", required = true) password: String
    ): ResponseEntity<Boolean> {
        return ResponseEntity.ok(authService.activateUser(username, passwordEncoder.encode(password)))
    }

    @PutMapping("Register")
    fun updateCredentials(
        @RequestBody(required = true) registration: RegistrationDTO,
        @RequestHeader("username", required = true) username: String,
        @RequestHeader("password", required = true) password: String
    ): ResponseEntity<Boolean> {
        val user = User()
        user.username = registration.username!!
        user.email = registration.email
        user.name = registration.name
        user.lastName = registration.lastName
        user.password = passwordEncoder.encode(registration.password)
        registration.roles?.forEach{
            user.roles += Role.valueOf(it)
        }
        registration.permissions?.forEach {
            user.permissions += Permission.valueOf(it)
        }
        return ResponseEntity.ok(authService.updateCredentials(username, passwordEncoder.encode(password), user))
    }
}