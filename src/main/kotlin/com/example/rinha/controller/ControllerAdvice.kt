package com.example.rinha.controller

import com.example.rinha.domain.exception.BadRequestException
import com.example.rinha.domain.exception.BaseException
import com.example.rinha.domain.exception.ErrorResponse
import com.example.rinha.domain.exception.NotFoundException
import com.example.rinha.domain.exception.UnprocessableException
import com.example.rinha.domain.exception.internalError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerAdvice {
    val logger = mu.KotlinLogging.logger {}

    @ExceptionHandler(BaseException::class)
    fun handleException(exception: Exception): ResponseEntity<ErrorResponse> {
        logger.error("Unhandled exception!", exception)
        internalError().errors
        return ResponseEntity
            .internalServerError()
            .body(ErrorResponse(errors = emptyList()))
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
