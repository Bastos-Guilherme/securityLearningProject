package com.securityLearningProject.securityLearningProject.controller.dto

// class for data transit
class RegistrationDTO {

    var username: String? = null
    var email: String? = null
    var name: String? = null
    var lastName: String? = null
    var password: String? = null
    var roles: List<String>? = null
    var permissions: List<String>? = null
}