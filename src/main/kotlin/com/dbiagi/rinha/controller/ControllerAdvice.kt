package com.dbiagi.rinha.controller

import com.dbiagi.rinha.domain.exception.BadRequestException
import com.dbiagi.rinha.domain.exception.BaseException
import com.dbiagi.rinha.domain.exception.ErrorResponse
import com.dbiagi.rinha.domain.exception.NotFoundException
import com.dbiagi.rinha.domain.exception.UnprocessableException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerAdvice {
    val logger = mu.KotlinLogging.logger {}

    @ExceptionHandler(BaseException::class)
    fun handleException(exception: BaseException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(toStatus(exception))
            .body(ErrorResponse(errors = exception.errors))
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(exception: NotFoundException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .build()

    private fun toStatus(exception: BaseException): HttpStatus = when (exception) {
        is BadRequestException -> HttpStatus.BAD_REQUEST
        is NotFoundException -> HttpStatus.NOT_FOUND
        is UnprocessableException -> HttpStatus.UNPROCESSABLE_ENTITY
        else -> HttpStatus.INTERNAL_SERVER_ERROR
    }
}
