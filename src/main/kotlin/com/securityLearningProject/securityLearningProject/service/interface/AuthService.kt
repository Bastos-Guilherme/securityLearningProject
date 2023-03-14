package com.securityLearningProject.securityLearningProject.service.`interface`

import com.securityLearningProject.securityLearningProject.controller.dto.TokenDTO
import com.securityLearningProject.securityLearningProject.model.User

interface AuthService {

    fun validadeUser(username: String, encodedPassword: String): Boolean

    fun inactivateUser(token: String): Boolean

    fun activateUser(username: String, password: String): Boolean

    fun getToken(username: String, password: String): TokenDTO

    fun isAccountExpired(user: User): Boolean

    fun isAccountLocked(user: User): Boolean

    fun isCredentialsExpired(user: User): Boolean

    fun isActive(user: User): Boolean
}