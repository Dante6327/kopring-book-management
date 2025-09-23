package com.project.bookmanagement.exception

class UserNotFoundException(message: String = "사용자를 찾을 수 없습니다.") : BusinessException(message)
class DuplicateEmailException(email: String) : BusinessException("이미 사용중인 이메일입니다: $email")
class DuplicateNicknameException(nickname:String) : BusinessException("이미 사용중인 닉네임입니다: $nickname")
class InvalidPasswordException(message: String = "비밀번호가 올바르지 않습니다.") : BusinessException(message)
class InactiveUserException(message: String = "비활성화된 계정입니다.") : BusinessException(message)
