package com.example.rinha.domain

data class TransactionRequest(
    val amount: Int,
    val type: TransactionType,
    val description: String
)
