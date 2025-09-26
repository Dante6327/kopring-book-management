package com.project.bookmanagement.config

import com.project.bookmanagement.service.AuthService
import com.project.bookmanagement.util.JwtToken
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter (
    private val jwtToken: JwtToken,
    private val authService: AuthService
): OncePerRequestFilter(){
    override fun doFilterInternal (
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ){
        try {
            println(request.getHeader(HttpHeaders.AUTHORIZATION))
            val jwt = getJwtFromRequest(request)
            if(jwt != null){
                val userId = jwtToken.getUserIdFromToken(jwt)
                if(jwtToken.validateToken(jwt, userId)){
                    val userId = jwtToken. getUserIdFromToken(jwt)
                    val user = authService.getCurrentUser(userId)

                    val authorities = listOf(SimpleGrantedAuthority("ROLE_${user.role}"))
                    val authentication = UsernamePasswordAuthenticationToken(
                        user.email,
                        null,
                        authorities
                    )

                    SecurityContextHolder.getContext().authentication = authentication

                    println("JWT 인증 성공 : ${user.email}, 권한 : ${user.role}")
                }
            } else {
                println("JWT 토큰 검증 실패")
            }

        }catch (e: Exception){
            println("JWT 인증 실패 : ${e.message}")
        }
        filterChain.doFilter(request, response)
    }

    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7, bearerToken.length)
        } else null
    }
}