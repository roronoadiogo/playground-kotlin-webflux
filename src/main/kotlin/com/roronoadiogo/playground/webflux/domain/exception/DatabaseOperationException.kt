package com.roronoadiogo.playground.webflux.domain.exception

class DatabaseOperationException(message: String, cause: Throwable) : RuntimeException(message, cause)