package com.securityLearningProject.securityLearningProject.controller.dto

import java.time.LocalDate
import java.util.*

// class for transfer of token to user
class TokenDTO {

    var token: String = ""
    var expirationDateTime: LocalDate = LocalDate.now()
}