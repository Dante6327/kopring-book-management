package com.project.bookmanagement.service

import com.project.bookmanagement.domain.User
import com.project.bookmanagement.dto.LoginRequest
import com.project.bookmanagement.dto.LoginResponse
import com.project.bookmanagement.dto.SignUpRequest
import com.project.bookmanagement.dto.UserInfo
import com.project.bookmanagement.exception.DuplicateEmailException
import com.project.bookmanagement.exception.DuplicateNicknameException
import com.project.bookmanagement.exception.InvalidPasswordException
import com.project.bookmanagement.exception.UserNotFoundException
import com.project.bookmanagement.repository.UserRepository
import com.project.bookmanagement.util.JwtToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService (
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtToken: JwtToken
){
    // 회원가입
    @Transactional
    fun signUp(signUpRequest: SignUpRequest) : UserInfo {

        // email 중복 검사
        if(userRepository.existsByEmail(signUpRequest.email)) {
            throw DuplicateEmailException(signUpRequest.email)
        }

        // 닉네임 중복 검사
        if(userRepository.existsByNickname(signUpRequest.nickname)) {
            throw DuplicateNicknameException(signUpRequest.nickname)
        }

        // 비밀번호 암호화
        val encodePassword = passwordEncoder.encode(signUpRequest.password)

        val user = User(
            email = signUpRequest.email,
            password = encodePassword,
            nickname = signUpRequest.nickname,
            role = User.UserRole.USER,
            provider = User.LoginProvider.LOCAL
        )

        val savedUser = userRepository.save(user)

        return UserInfo(
            id = savedUser.id,
            email = savedUser.email,
            nickname = savedUser.nickname,
            role = savedUser.role.name
        )
    }

    fun login(loginRequest: LoginRequest): LoginResponse {
        val user = userRepository.findByEmailAndIsActiveTrue(loginRequest.email)
            ?: throw UserNotFoundException("이메일 또는 비밀번호가 올바르지 않습니다.")

        if (!passwordEncoder.matches(loginRequest.password, user.password)) {
            throw InvalidPasswordException("이메일 또는 비밀번호가 올바르지 않습니다.")
        }

        val accessToken = jwtToken.generateToken(
            userId = user.id,
            email = user.email,
            role = user.role.name
        )

        val userInfo = UserInfo(
            id = user.id,
            email = user.email,
            nickname = user.nickname,
            role = user.role.name
        )

        return LoginResponse(
            accessToken = accessToken,
            user = userInfo
        )
    }

    // 사용자 정보 조회 (토큰 기반)
    fun getCurrentUser(userId: Long): UserInfo {
        val user = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException() }

        return UserInfo(
            id = user.id,
            email = user.email,
            nickname = user.nickname,
            role = user.role.name
        )
    }

    fun validateToken(token: String): Boolean {
        return try {
            jwtToken.isValidToken(token)
        } catch (e: Exception) {
            false
        }
    }
}