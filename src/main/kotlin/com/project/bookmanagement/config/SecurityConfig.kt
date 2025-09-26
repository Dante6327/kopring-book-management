package com.project.bookmanagement.config

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    // 일단 테스트 단계에서 모든 요청 허용
    @Bean
    fun filterChain(http: HttpSecurity, jwtAuthenticationFilter: JwtAuthenticationFilter): SecurityFilterChain {
        http
            .csrf { it.disable() }  // CSRF 비활성화 (JWT 사용하므로)
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // 세션 사용 안함
            .anonymous { it.disable() }
            .exceptionHandling { ex ->
                ex.authenticationEntryPoint { request, response, authException ->
                    println("🚨 인증 실패: ${authException.message}")
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                }
            }
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers("/api/auth/**").permitAll()  // 인증 관련 API는 모두 허용
                    .requestMatchers("/h2-console/**").permitAll() // H2 콘솔 허용
                    .requestMatchers("/api/books/**").authenticated()  // 일단 도서 API도 허용 (나중에 수정)
                    .anyRequest().authenticated()  // 나머지는 인증 필요
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .headers { it.frameOptions { frameOptions -> frameOptions.disable() } } // H2 콘솔용

        return http.build()
    }
}