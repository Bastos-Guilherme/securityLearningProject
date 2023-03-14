package com.securityLearningProject.securityLearningProject.exception

class UnauthorizedUserException(message: String, cause: Throwable): AuthException("$message", cause) {}