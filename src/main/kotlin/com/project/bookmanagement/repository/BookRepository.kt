package com.project.bookmanagement.repository

import com.project.bookmanagement.domain.Book
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<Book, Long> {
    fun existsByIsbn(isbn: String): Boolean
    fun findByIsbn(isbn: String): Book?
}