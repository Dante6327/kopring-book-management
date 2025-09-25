package com.project.bookmanagement.controller

import com.project.bookmanagement.dto.ApiResponse
import com.project.bookmanagement.dto.LoginRequest
import com.project.bookmanagement.dto.LoginResponse
import com.project.bookmanagement.dto.ResponseUtil
import com.project.bookmanagement.dto.SignUpRequest
import com.project.bookmanagement.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.http.HttpResponse

@RestController
@RequestMapping("/api/auth")
class AuthController (
    private val authService: AuthService,
){
    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody request: SignUpRequest) : ResponseEntity<ApiResponse<String>>{
        authService.signUp(request)
        return ResponseEntity.ok(ResponseUtil.success(message = "회원가입이 완료되었습니다."))
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest) : ResponseEntity<ApiResponse<LoginResponse>> {
        val loginResponse = authService.login(request)
        return ResponseEntity.ok(ResponseUtil.success(data = loginResponse, message = "로그인에 성공했습니다."))
    }

    @PostMapping("/validate")
    fun validateToken(@RequestHeader("Authorization") authHeader: String): ResponseEntity<ApiResponse<String>>{
        println(authHeader)
        val token = authHeader.removePrefix("Bearer ")
        val isValid = authService.validateToken(token)

        return if(isValid) {
            ResponseEntity.ok(ResponseUtil.success(message = "유효한 토큰입니다."))
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtil.error(message = "유효하지 않은 토큰입니다."))
        }
    }
}