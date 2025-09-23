package com.project.bookmanagement.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    val email: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val nickname: String,

    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.USER,

    @Enumerated(EnumType.STRING)
    val provider: LoginProvider = LoginProvider.LOCAL,

    val isActive: Boolean = true
) :BaseEntity() {

    enum class UserRole {
        USER, ADMIN
    }

    enum class LoginProvider {
        LOCAL, GOOGLE, KAKAO
    }
}