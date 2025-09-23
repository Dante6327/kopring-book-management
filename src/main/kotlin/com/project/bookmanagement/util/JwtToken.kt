package com.project.bookmanagement.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date

@Component
class JwtToken {

    // application.properties에서 설정값 가져오기
    @Value("\${jwt.secret}")
    private lateinit var jwtSecret: String

    // 24시간
    @Value("\${jwt.expiration}")
    private val jwtExpiration: Long = 86400

    // 암호화 키 생성
    private fun getSigningKey(): Key {
        return Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }

    // JWT 토큰 생성
    fun generateToken(userId: Long?, email: String, role: String): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtExpiration * 1000)

        return Jwts.builder()
            .setSubject(userId.toString())
            .claim("email", email)
            .claim("role", role)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    // 토큰에서 사용자 ID일 추출
    fun getUserIdFromToken(token: String): Long {
        return getClaimsFromToken(token).subject.toLong()
    }

    // 토큰에서 사용자 Email 추출
    fun getUserEmailFromToken(token: String): String {
        return getClaimsFromToken(token).get("email").toString()
    }

    // 토큰에서 유저 타입 추출
    fun getRoleFromToken(token: String): String {
        return getClaimsFromToken(token).get("role").toString()
    }

    // 토큰 만료시간 확인
    fun getExpirationDateFromToken(token: String): Date {
        return getClaimsFromToken(token).expiration
    }

    // 토큰 유효성 검증 - ID로 검증
    fun validateToken(token: String, userId: Long): Boolean {
        return try {
            val userIdFromToken = getUserIdFromToken(token)
            userIdFromToken == userId && !isTokenExpired(token)
        } catch (e: Exception) {
            false
        }
    }

    // 토큰 유효성 단순 검증
    fun isValidToken(token: String): Boolean {
        return try {
            !isTokenExpired(token)
        } catch (e: Exception) {
            false
        }
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getClaimsFromToken(token).expiration
        return expiration.before(Date())
    }

    private fun getClaimsFromToken(token: String): Claims {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            throw RuntimeException("토큰 파싱 오류: ${e.message}", e)
        }
    }
}