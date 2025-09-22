import com.project.bookmanagement.dto.ApiResponse
import com.project.bookmanagement.dto.ResponseUtil
import com.project.bookmanagement.exception.BookNotFoundException
import com.project.bookmanagement.exception.BusinessException
import com.project.bookmanagement.exception.DuplicateIsbnException
import com.project.bookmanagement.exception.InvalidBookDataException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

// exception/GlobalExceptionHandler.kt
@RestControllerAdvice
@Component
class GlobalExceptionHandler {
    // 도서를 찾을 수 없음
    @ExceptionHandler(BookNotFoundException::class)
    fun handleBookNotFoundException(ex: BookNotFoundException): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ResponseUtil.error(ex.message ?: "도서를 찾을 수 없습니다."))
    }

    // ISBN 중복
    @ExceptionHandler(DuplicateIsbnException::class)
    fun handleDuplicateIsbnException(ex: DuplicateIsbnException): ResponseEntity<ApiResponse<Unit>> {
        println("13212312312312312312312 $ex.message")
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ResponseUtil.error(ex.message ?: "중복된 ISBN입니다."))
    }

    // 잘못된 도서 데이터
    @ExceptionHandler(InvalidBookDataException::class)
    fun handleInvalidBookDataException(ex: InvalidBookDataException): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseUtil.error(ex.message ?: "잘못된 도서 정보입니다."))
    }

    // 비즈니스 예외 (상위 클래스)
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseUtil.error(ex.message ?: "비즈니스 로직 오류가 발생했습니다."))
    }

    // 기존 예외들...
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ResponseUtil.error(ex.message ?: "요청한 리소스를 찾을 수 없습니다."))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Unit>> {
        val errorMessage = ex.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseUtil.error("입력 데이터가 올바르지 않습니다. $errorMessage"))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ResponseUtil.error("서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요."))
    }
}