package com.project.bookmanagement.controller

import com.project.bookmanagement.domain.Book
import com.project.bookmanagement.dto.ApiResponse
import com.project.bookmanagement.dto.BookRequest
import com.project.bookmanagement.dto.ResponseUtil
import com.project.bookmanagement.service.BookService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/books")
class BookController(
    private val bookService: BookService
) {
    // 도서 등록
    @PostMapping
    fun createBook(@Valid @RequestBody bookRequest: BookRequest): ResponseEntity<ApiResponse<Book>> {
        println(bookRequest)
        val createdBook = bookService.createBook(bookRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtil.success(createdBook, message = "도서가 성공적으로 등록되었습니다."))
    }

    // 전체 도서 조회
    @GetMapping
    fun getAllBooks(): ResponseEntity<ApiResponse<List<Book>>> {
        val books = bookService.findAll()
        return ResponseEntity.ok(ResponseUtil.success(books, "도서 조회 성공"))
    }

    @GetMapping("/{id}")
    fun getBook(@PathVariable("id") id: Long): ResponseEntity<ApiResponse<Book>> {
        val book = bookService.findById(id)
        return ResponseEntity.ok(ResponseUtil.success(book, message = "도서 1건 조회 성공"))
    }

    // 도서 수정
    @PutMapping("/{id}")
    fun updateBook(
        @PathVariable(value = "id") id: Long,
        @Valid @RequestBody bookRequest: BookRequest
    ): ResponseEntity<ApiResponse<Book>> {
        val updatedBook = bookService.updateBook(id, bookRequest)
        return ResponseEntity.ok(ResponseUtil.success(updatedBook,  "도서 수정 성공"))
    }

    // 도서 삭제
    @DeleteMapping("/{id}")
    fun deleteBook(@PathVariable(value = "id") id: Long): ResponseEntity<ApiResponse<Unit>> {
        bookService.deleteBook(id)
        return ResponseEntity.ok(ResponseUtil.success(Unit, message = "도서 삭제 성공"))
    }
}