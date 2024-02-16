package com.dbiagi.rinha.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class TransactionBalanceResponse (
    @JsonProperty("valor")
    val amount: Int,

    @JsonProperty("tipo")
    val type: TransactionType,

    @JsonProperty("descricao")
    val desciption: String,

    @JsonProperty("realizada_em")
    val createdAt: LocalDateTime
)
