package com.hackaction.sidul_restful_api.core.services

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class JwtService {
    // Local constanta within the JwtService Class
    // TODO move it to application.properties
    val secretKey: String = "ZTYxMjI3YzU3MzRkMjA1ODVhZmVmNDc3ZjM5ZTQ4MmJhODkxZWZiOTIxZDcxZjExZGQ5MGY4YjZlYjBlZTc4Yw"

    // Generate token only with user details
    fun generateToken(userDetails: UserDetails) = generateToken(emptyMap(), userDetails)

    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String =
        Jwts.builder().setClaims(extraClaims).setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();

    fun getSignInKey(): Key {
        val keyBytes: ByteArray = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun extractUsername(jwtToken: String): String = extractClaim(jwtToken) { claims -> claims.subject }

    fun extractAllClaims(jwtToken: String): Claims =
        Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(jwtToken).body

    fun <T> extractClaim(jwtToken: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(jwtToken)
        return claimsResolver(claims)
    }

    fun extractExpiration(jwtToken: String): Date = extractClaim(jwtToken) { claims -> claims.expiration }

    fun isJwtTokenValid(jwtToken: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(jwtToken)
        return username == userDetails.username && !isTokenExpired(jwtToken)
    }

    fun isTokenExpired(jwtToken: String): Boolean = extractExpiration(jwtToken).before(Date())
}
