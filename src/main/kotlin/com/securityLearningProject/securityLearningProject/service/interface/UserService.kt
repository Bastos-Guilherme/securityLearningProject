package com.securityLearningProject.securityLearningProject.service.`interface`

import com.securityLearningProject.securityLearningProject.model.User

interface UserService {

    fun findById(id: Long): User

    fun findByEmail(email: String): User

    fun findByUsername(username: String): User

    fun deleteById(id: Long): Boolean

    fun validadeUser(username: String, password: String): Boolean

    fun register(user: User): Boolean

    fun invalidateToken(token: String): Boolean

    fun activateUser(username: String, password: String): Boolean
}