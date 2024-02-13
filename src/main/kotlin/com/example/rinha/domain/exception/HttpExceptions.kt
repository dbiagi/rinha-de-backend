package com.example.rinha.domain.exception

open class NotFoundException : BaseException(errors = listOf(AppError("not_found", "Not found")))
open class BadRequestException(errors: List<AppError> = emptyList()) : BaseException(errors)
open class UnauthorizedException(errors: List<AppError> = emptyList()) : BaseException(errors)
open class UnprocessableException(errors: List<AppError> = emptyList()) : BaseException(errors)

fun notFound(error: AppError): NotFoundException = NotFoundException()

fun badRequest(errorCode: String, message: String): BadRequestException =
    BadRequestException(listOf(AppError(errorCode, message)))

fun internalError(): BaseException = BaseException(emptyList())

fun unprocessableEntity(errorCode: String, message: String): UnprocessableException =
    UnprocessableException(listOf(AppError(errorCode, message)))
