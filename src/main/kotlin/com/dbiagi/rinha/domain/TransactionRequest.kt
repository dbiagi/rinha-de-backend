package com.dbiagi.rinha.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class TransactionRequest(
    @JsonProperty("valor")
    val amount: Int,

    @JsonProperty("tipo")
    val type: TransactionType,

    @JsonProperty("descricao")
    val description: String,
)
