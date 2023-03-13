package com.securityLearningProject.securityLearningProject.controller.dto

import java.time.LocalDateTime
import java.util.Date

// class for transfer of token to user
class TokenDTO {

    var token: String = ""
    var expirationDateTime: Date = Date(System.currentTimeMillis())
}