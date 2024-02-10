package com.example.rinha.domain.exception

open class BaseException(
    val errors: List<AppError> = emptyList(),
    override val cause: Throwable? = null,
    override val message: String? = null
) : RuntimeException(cause)
