package com.dbiagi.rinha.repository

import com.dbiagi.rinha.model.Transaction
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TransactionRepository : R2dbcRepository<Transaction, Int> {
    fun findAllByAccountId(accountId: Int): Flux<Transaction>
}
