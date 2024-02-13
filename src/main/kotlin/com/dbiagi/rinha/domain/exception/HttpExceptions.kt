package com.dbiagi.rinha.domain.exception

open class NotFoundException : BaseException(errors = listOf(AppError("not_found", "Not found")))
open class BadRequestException(errors: List<AppError> = emptyList()) : BaseException(errors)
open class UnauthorizedException(errors: List<AppError> = emptyList()) : BaseException(errors)
open class UnprocessableException(errors: List<AppError> = emptyList()) : BaseException(errors)
