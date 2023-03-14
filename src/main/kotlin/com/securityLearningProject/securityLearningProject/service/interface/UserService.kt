package com.securityLearningProject.securityLearningProject.service.`interface`

import com.securityLearningProject.securityLearningProject.model.User

interface UserService {

    fun findByUsername(username: String): User

    fun findByEmail(email: String): User

    fun register(user: User): Boolean
}