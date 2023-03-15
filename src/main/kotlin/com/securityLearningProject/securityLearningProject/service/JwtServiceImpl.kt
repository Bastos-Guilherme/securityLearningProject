package com.securityLearningProject.securityLearningProject.service

import com.securityLearningProject.securityLearningProject.model.User
import com.securityLearningProject.securityLearningProject.service.`interface`.JwtService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.security.Key
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Function
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

// service for manipulation of the jwt
@Service
class JwtServiceImpl: JwtService {

    // key for sign the jwt`s, very important to make secure and off the application
    companion object {
        private const val SECRET_KEY: String = "597133743677397A244226452948404D635166546A576E5A7234753778214125"
    }

    // generation of the jwt token using dependency
    override fun generateToken(extraClaims: Map<String, JvmType.Object>, user: User): String {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(user.username)
            .claim("authorities", user.authorities)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30)))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    // optional generation of the jwt
    override fun genarateToken(user: User): String {
        return generateToken(emptyMap(), user)
    }

    // validation of the jwt for a user
    override fun isTokenValid(token: String, user: User): Boolean {
        val username: String = extractUsername(token)
        return username.equals(user.username) && !isTokenExpired(token)
    }

    // validation of expiration for any given jwt
    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).isBefore(LocalDate.now())
    }

    // extraction of username from a jwt, same format to extract anything from the jwt
    override fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    // abstraction of the exctraction
    override fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    // extraction of the expiration date
    override fun extractExpiration(token: String): LocalDate {
        return extractClaim(token, Claims::getExpiration).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    // extraction of the claims
    private fun extractAllClaims(token: String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    // function for the hashing of the key
    private fun getSignInKey(): Key {
        val keyBytes: ByteArray = Decoders.BASE64.decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}