package com.securityLearningProject.securityLearningProject.service.`interface`

import com.securityLearningProject.securityLearningProject.model.User
import io.jsonwebtoken.Claims
import java.util.*
import java.util.function.Function
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

interface JwtService {

    fun extractUsername(token: String): String

    fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T

    fun generateToken(extraClaims: Map<String, JvmType.Object>, user: User): String

    fun genarateToken(user: User): String

    fun isTokenValid(token: String, user : User): Boolean

    fun extractExpiration(token: String): Date
}