package com.project.bookmanagement.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    // 일단 테스트 단계에서 모든 요청 허용
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }  // CSRF 비활성화 (JWT 사용하므로)
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // 세션 사용 안함
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers("/api/auth/**").permitAll()  // 인증 관련 API는 모두 허용
                    .requestMatchers("/h2-console/**").permitAll() // H2 콘솔 허용
                    .requestMatchers("/api/books/**").permitAll()  // 일단 도서 API도 허용 (나중에 수정)
                    .anyRequest().authenticated()  // 나머지는 인증 필요
            }
            .headers { it.frameOptions { frameOptions -> frameOptions.disable() } } // H2 콘솔용

        return http.build()
    }
}