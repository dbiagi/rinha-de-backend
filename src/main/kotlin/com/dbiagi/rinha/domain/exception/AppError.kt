package com.dbiagi.rinha.domain.exception

data class AppError(
    val code: String = "",
    val message: String = "",
    val arguments: Map<String, String> = emptyMap()
)
