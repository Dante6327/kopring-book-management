package com.project.bookmanagement.exception

class BookNotFoundException(bookId: Long) : RuntimeException("도서를 찾을 수 없습니다. ID: $bookId")
class DuplicateIsbnException(isbn: String) : RuntimeException("이미 존재하는 ISBN입니다. $isbn")
class InvalidBookDataException(message: String) : RuntimeException("잘못된 도서 정보입니다. $message")

abstract class BusinessException(message: String) : RuntimeException(message)
class ResourceNotFoundException(message: String) : BusinessException(message)
class DuplicateResourceException(message: String) : BusinessException(message)