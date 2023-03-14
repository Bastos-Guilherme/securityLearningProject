package com.securityLearningProject.securityLearningProject.service

import com.securityLearningProject.securityLearningProject.model.User
import com.securityLearningProject.securityLearningProject.model.repository.UserRepository
import com.securityLearningProject.securityLearningProject.service.`interface`.UserService
import org.springframework.beans.factory.annotation.Autowired

class UserServiceImpl: UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun findByUsername(username: String): User {
        return userRepository.findByUsername(username).orElseThrow()
    }

    override fun findByEmail(email: String): User {
        return userRepository.findByEmail(email).orElseThrow()
    }

    override fun register(user: User): Boolean {
        userRepository.save(user)
        return true
    }
}