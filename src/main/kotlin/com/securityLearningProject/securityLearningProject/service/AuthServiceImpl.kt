package com.securityLearningProject.securityLearningProject.service

import com.securityLearningProject.securityLearningProject.controller.dto.TokenDTO
import com.securityLearningProject.securityLearningProject.exception.UnauthorizedUserException
import com.securityLearningProject.securityLearningProject.model.User
import com.securityLearningProject.securityLearningProject.service.`interface`.AuthService
import com.securityLearningProject.securityLearningProject.service.`interface`.JwtService
import com.securityLearningProject.securityLearningProject.service.`interface`.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

// all uses for the user
@Service
class AuthServiceImpl: AuthService {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var jwtService: JwtService

    // validade if user is valid, meaning excists, credentials correct, credentials valid and nom expire
    override fun validadeUser(username: String, password: String): Boolean {
        val user = userService.findByUsername(username)
        if (user.password == password) {
            if (isActive(user)) {
                if (isAccountExpired(user)) {
                    if (isCredentialsExpired(user)) {
                        if (isAccountLocked(user)) {
                            return true
                        } else {
                            user.failedLoginAttempts++
                            throw UnauthorizedUserException("Account locked", Throwable("AuthService: validadeUser"))
                        }
                    } else {
                        user.failedLoginAttempts++
                        throw UnauthorizedUserException("Credentials expired", Throwable("AuthService: validadeUser"))
                    }
                } else {
                    user.failedLoginAttempts++
                    throw UnauthorizedUserException("Account expired", Throwable("AuthService: validadeUser"))
                }
            } else {
                user.failedLoginAttempts++
                throw UnauthorizedUserException("User inactive", Throwable("AuthService: validadeUser"))
            }
        } else {
            user.failedLoginAttempts++
            throw UnauthorizedUserException("Incorrect password", Throwable("AuthService: validadeUser"))
        }
    }

    // invalidade user, not implemented, does nothing as of right now
    override fun inactivateUser(token: String): Boolean {
        var cleanToken = ""
        if (token.startsWith("Bearer ")) {
            cleanToken = token.substring(7)
        } else {
            cleanToken = token
        }
        val user = userService.findByUsername(jwtService.extractUsername(cleanToken))
        if (isAccountExpired(user)) {
            if (isCredentialsExpired(user)) {
                if (isAccountLocked(user)) {
                    user.isActive = false
                    return true
                }
            }
        }
        return false
    }

    // activate user
    override fun activateUser(username: String, password: String): Boolean {
        val user = userService.findByUsername(username)
        if (user.password == password) {
            if (isAccountExpired(user)) {
                if (isCredentialsExpired(user)) {
                    if (isAccountLocked(user)) {
                        user.isActive = false
                        return true
                    }
                }
            }
        }
        user.failedLoginAttempts++
        return false
    }

    override fun getToken(username: String, password: String): TokenDTO {
        validadeUser(username, password)
        val user = userService.findByUsername(username)
        val tokenResponse = TokenDTO()
        tokenResponse.token = jwtService.genarateToken(user)
        tokenResponse.expirationDateTime = jwtService.extractExpiration(tokenResponse.token)
        return tokenResponse
    }

    override fun isAccountExpired(user: User): Boolean {
        return user.lastSigning.isBefore(LocalDate.now().plusMonths(3))
    }

    override fun isAccountLocked(user: User): Boolean {
        return user.failedLoginAttempts > 5
    }

    override fun isCredentialsExpired(user: User): Boolean {
        return user.credentialsLastChange.isBefore(LocalDate.now().plusYears(3))
    }

    override fun isActive(user: User): Boolean {
        return user.isActive
    }
}