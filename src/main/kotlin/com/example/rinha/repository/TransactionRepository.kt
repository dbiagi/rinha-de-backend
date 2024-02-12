package com.example.rinha.repository

import com.example.rinha.model.Transaction
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux

interface TransactionRepository : R2dbcRepository<Transaction, Int> {
    fun findAllByAccountId(accountId: Int): Flux<Transaction>
}
