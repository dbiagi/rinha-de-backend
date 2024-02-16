package com.dbiagi.rinha.service

import com.dbiagi.rinha.domain.TransactionRequest
import com.dbiagi.rinha.domain.TransactionResponse
import com.dbiagi.rinha.domain.TransactionType
import com.dbiagi.rinha.domain.exception.NotEnoughBalance
import com.dbiagi.rinha.domain.exception.NotFoundException
import com.dbiagi.rinha.model.Account
import com.dbiagi.rinha.model.Transaction
import com.dbiagi.rinha.repository.TransactionRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.util.function.Tuple2

@Service
class TransactionService(
    private val accountService: AccountService,
    private val transactionRepository: TransactionRepository,
    private val balanceService: BalanceService
) {
    fun create(accountId: Int, request: TransactionRequest): Mono<TransactionResponse> =
        Mono.defer {
            accountService.getAccount(accountId)
        }.switchIfEmpty {
            Mono.error(NotFoundException())
        }.flatMap { account: Account ->
            getAvailableBalance(account, request).zipWith(Mono.just(account))
        }.map { t: Tuple2<Int, Account> ->
            val currentBalanceAfterTransaction = when (request.type) {
                TransactionType.DEBIT -> t.t1 - request.amount
                TransactionType.CREDIT -> t.t1 + request.amount
            }

            transactionRepository.save(mapToTransaction(t.t2, request)).map {
                TransactionResponse(t.t2.limit, currentBalanceAfterTransaction)
            }
        }.flatMap { it }

    private fun getAvailableBalance(
        account: Account,
        requestedTransaction: TransactionRequest
    ): Mono<Int> {
        val hasAvailableLimit: (TransactionRequest, Int) -> Boolean =
            { request: TransactionRequest, currentBalance: Int ->
                if (request.type == TransactionType.DEBIT) (currentBalance - request.amount) >= -account.limit
                else true
            }

        return balanceService.getBalance(account.id)
            .flatMap { currentBalance ->
                if (hasAvailableLimit(requestedTransaction, currentBalance))
                    Mono.just(currentBalance)
                else
                    Mono.error(NotEnoughBalance())
            }
    }

    private fun mapToTransaction(account: Account, request: TransactionRequest): Transaction = Transaction(
        accountId = account.id,
        amount = request.amount,
        type = request.type,
        description = request.description
    )
}
