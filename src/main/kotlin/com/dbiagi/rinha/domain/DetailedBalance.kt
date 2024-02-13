package com.dbiagi.rinha.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class DetailedBalance(
    @JsonProperty("saldo")
    val balance: Balance,
    @JsonProperty("ultimas_transacoes")
    val lastTransactions: List<TransactionResponse>,
)


