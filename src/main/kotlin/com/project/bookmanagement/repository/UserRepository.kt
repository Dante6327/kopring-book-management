package com.project.bookmanagement.repository

import com.project.bookmanagement.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    fun existsByNickname(nickname: String): Boolean

    // 활성화된 사용자만 조회 (이메일 + 활성화)
    fun findByEmailAndIsActiveTrue(email: String): User?

    // 토큰 검증 (ID + 활성화)
    fun findByIdAndIsActiveTrue(id: Long): User?
}