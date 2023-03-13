package com.securityLearningProject.securityLearningProject.model.repository

import com.securityLearningProject.securityLearningProject.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

// repository, for DB purposes
@Repository
interface UserRepository: JpaRepository<User, Long> {

    fun findByEmail(email: String): Optional<User>

    fun findByUsername(username: String): Optional<User>
}