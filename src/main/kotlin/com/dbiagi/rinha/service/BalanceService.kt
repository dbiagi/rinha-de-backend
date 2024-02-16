package com.dbiagi.rinha.service

import com.dbiagi.rinha.domain.Balance
import com.dbiagi.rinha.domain.DetailedBalance
import com.dbiagi.rinha.domain.TransactionBalanceResponse
import com.dbiagi.rinha.domain.TransactionType
import com.dbiagi.rinha.domain.exception.NotFoundException
import com.dbiagi.rinha.repository.TransactionRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.LocalDateTime

@Service
class BalanceService(
    private val transactionRepository: TransactionRepository,
    private val accountService: AccountService
) {
    fun getBalance(accountId: Int): Mono<Int> {
        return transactionRepository.findAllByAccountId(accountId).reduce(0) { agg, t ->
            when (t.type) {
                TransactionType.CREDIT -> agg + t.amount
                TransactionType.DEBIT -> agg - t.amount
            }
        }
    }

    fun getDetailedBalance(accountId: Int): Mono<DetailedBalance> = accountService.getAccount(accountId)
        .switchIfEmpty { Mono.error(NotFoundException()) }
        .flatMap { account ->
            getTransactions(accountId).map {
                DetailedBalance(Balance(calculateTotal(it), account.limit, LocalDateTime.now()), it)
            }
        }

    private fun calculateTotal(transactions: List<TransactionBalanceResponse>): Int = transactions.sumOf { t ->
        when (t.type) {
            TransactionType.CREDIT -> t.amount
            TransactionType.DEBIT -> -t.amount
        }
    }

    private fun getTransactions(accountId: Int): Mono<List<TransactionBalanceResponse>> =
        transactionRepository.findAllByAccountId(accountId)
            .map { TransactionBalanceResponse(it.amount, it.type, it.description, it.createdAt) }
            .collectList()
}
