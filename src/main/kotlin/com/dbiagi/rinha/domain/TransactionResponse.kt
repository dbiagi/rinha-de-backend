package com.dbiagi.rinha.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class TransactionResponse (
    @JsonProperty("limite")
    val limit: Int,

    @JsonProperty("saldo")
    val balance: Int
)
