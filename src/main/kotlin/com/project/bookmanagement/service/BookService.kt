package com.project.bookmanagement.service

import com.project.bookmanagement.domain.Book
import com.project.bookmanagement.dto.BookRequest
import com.project.bookmanagement.exception.BookNotFoundException
import com.project.bookmanagement.exception.DuplicateIsbnException
import com.project.bookmanagement.repository.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class BookService(
    private val bookRepository: BookRepository
) {
    @Transactional
    fun createBook(bookRequest: BookRequest): Book {
        // ISBN 중복 체크
        if (bookRepository.existsByIsbn(bookRequest.isbn)) {
            throw DuplicateIsbnException(bookRequest.isbn)
        }
        return bookRepository.save(bookRequest.toEntity())
    }

    fun findById(id: Long): Book {
        return bookRepository.findById(id)
            .orElseThrow { BookNotFoundException(id) }
    }

    fun findAll(): List<Book> {
        return bookRepository.findAll()
    }

    @Transactional
    fun updateBook(id: Long, bookRequest: BookRequest): Book {
        val existingBook = findById(id)

        // 다른 도서가 같은 ISBN을 사용하는지 체크
        bookRepository.findByIsbn(bookRequest.isbn)?.let {foundBook ->
            if (foundBook.id != id) {
                throw DuplicateIsbnException(bookRequest.isbn)
            }
        }

        val updatedBook = Book(
            id = existingBook.id,
            title = bookRequest.title,
            author = bookRequest.author,
            isbn = bookRequest.isbn,
            publishedDate = bookRequest.publishedDate,
        )
        return bookRepository.save(updatedBook)
    }

    @Transactional
    fun deleteBook(id: Long) {
        if (!bookRepository.existsById(id)) {
            throw BookNotFoundException(id)
        }
        bookRepository.deleteById(id)
    }
}