package com.securityLearningProject.securityLearningProject.service

import com.securityLearningProject.securityLearningProject.model.User
import com.securityLearningProject.securityLearningProject.model.repository.UserRepository
import com.securityLearningProject.securityLearningProject.service.`interface`.JwtService
import com.securityLearningProject.securityLearningProject.service.`interface`.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

// all uses for the user
@Service
class UserServiceImpl: UserService {

    @Autowired
    private lateinit var userRepository: UserRepository


    // find any user by id, throw exception if fail to find one
    override fun findById(id: Long): User {
        return userRepository.findById(id).orElseThrow()
    }

    // find any user by email, throw exception if fail to find one
    override fun findByEmail(email: String): User {
        return userRepository.findByEmail(email).orElseThrow()
    }

    // find any user by username, throw exception if fail to find one
    override fun findByUsername(username: String): User {
        return userRepository.findByUsername(username).orElseThrow()
    }

    // delete any user by id, throw exception if fail to find one
    override fun deleteById(id: Long): Boolean {
        userRepository.deleteById(id)
        return true
    }

    // validade if user have credential
    override fun validadeUser(username: String, encodedPassword: String): Boolean {
        val user: User = findByUsername(username)
        if (user.password == encodedPassword)
            return true
        return false
    }

    // create new user with data
    override fun register(user: User): Boolean {
        userRepository.save(user)
        return true
    }

    // invalidade user, not implemented, does nothing as of right now
    override fun invalidateToken(token: String): Boolean {
        return false
    }
}