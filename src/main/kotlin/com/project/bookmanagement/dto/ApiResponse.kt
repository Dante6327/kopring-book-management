package com.project.bookmanagement.dto

import java.time.LocalDateTime

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val timeStamp: String = LocalDateTime.now().toString(),
)

object ResponseUtil {
    fun <T> success(data: T, message: String = "성공"): ApiResponse<T> {
        return ApiResponse(success = true, data = data, message = message)
    }

    fun <T> success(message: String): ApiResponse<T> {
        return ApiResponse(success = true, data = null, message = message)
    }

    fun <T> error(message: String): ApiResponse<T> {
        return ApiResponse(success = false, message = message)
    }
}
