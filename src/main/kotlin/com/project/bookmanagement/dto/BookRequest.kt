package com.project.bookmanagement.dto
import com.project.bookmanagement.domain.Book
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.time.LocalDate

data class BookRequest(
    @field:NotBlank(message = "제목은 필수입니다.")
    val title: String,

    @field:NotBlank(message = "저자는 필수입니다.")
    val author: String,

    @field:Pattern(regexp = "^\\d{10}$|^\\d{13}$", message = "올바른 ISBN 형식이 아닙니다.")
    val isbn: String,

    val publishedDate: LocalDate
) {
    fun toEntity(): Book {
        return Book(
            title = this.title,
            author = this.author,
            isbn = isbn,
            publishedDate = this.publishedDate
        )
    }
}
