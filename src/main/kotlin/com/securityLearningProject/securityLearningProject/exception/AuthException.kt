package com.securityLearningProject.securityLearningProject.exception

abstract class AuthException: RuntimeException {
    constructor(message: String?) : super(message) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}
}