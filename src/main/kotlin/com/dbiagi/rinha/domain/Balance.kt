package com.dbiagi.rinha.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class Balance(
    val total: Int,
    @JsonProperty("limite")
    val limit: Int,
    @JsonProperty("data_extrato")
    val createdAt: LocalDateTime
)
