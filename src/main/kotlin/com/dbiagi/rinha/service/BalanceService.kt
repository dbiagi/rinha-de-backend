package com.dbiagi.rinha.service

import com.dbiagi.rinha.domain.TransactionType
import com.dbiagi.rinha.repository.TransactionRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class BalanceService(
    private val transactionRepository: TransactionRepository
) {
    fun getBalance(accountId: Int): Mono<Int> {
        return transactionRepository.findAllByAccountId(accountId).reduce(0) { agg, t ->
            when (t.type) {
                TransactionType.CREDIT -> agg + t.amount
                TransactionType.DEBIT -> agg - t.amount
            }
        }
    }
}
