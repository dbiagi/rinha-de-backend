package com.dbiagi.rinha.domain.exception

class NotFoundException(description: String = "") : BaseException(message = description)

class BadRequestException(errors: List<AppError> = emptyList()) : BaseException(errors)
class UnauthorizedException(errors: List<AppError> = emptyList()) : BaseException(errors)
class UnprocessableException(errors: List<AppError> = emptyList()) : BaseException(errors)

fun notFound(error: AppError): NotFoundException = NotFoundException()

fun badRequest(errorCode: String, message: String, args: Map<String, String> = emptyMap()): BadRequestException =
    BadRequestException(listOf(AppError(errorCode, message, args)))

fun internalError(): BaseException = BaseException(emptyList())

fun unprocessableEntity(
    errorCode: String,
    message: String,
    args: Map<String, String> = emptyMap()
): UnprocessableException =
    UnprocessableException(listOf(AppError(errorCode, message, args)))
