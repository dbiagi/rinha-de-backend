package com.dbiagi.rinha.domain

import com.fasterxml.jackson.annotation.JsonProperty

enum class TransactionType {
    @JsonProperty("c")
    CREDIT,

    @JsonProperty("d")
    DEBIT
}

