package com.printcoststudio.backend.common

class InvalidCredentialsException(
    message: String,
) : RuntimeException(message)

class InvalidTokenException(
    message: String,
) : RuntimeException(message)
