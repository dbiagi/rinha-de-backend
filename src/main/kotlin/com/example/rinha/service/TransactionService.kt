package com.example.rinha.service

import com.example.rinha.domain.TransactionRequest
import com.example.rinha.domain.TransactionType
import com.example.rinha.domain.exception.NotEnoughBalance
import com.example.rinha.domain.exception.NotFoundException
import com.example.rinha.model.Account
import com.example.rinha.model.Transaction
import com.example.rinha.repository.TransactionRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class TransactionService(
    private val accountService: AccountService,
    private val transactionRepository: TransactionRepository,
    private val balanceService: BalanceService
) {
    fun create(accountId: Int, request: TransactionRequest): Mono<Void> =
        Mono.defer {
            accountService.getAccount(accountId)
        }.switchIfEmpty {
            Mono.error(NotFoundException())
        }.flatMap { account: Account ->
            hasAvailableBalance(account, request)
        }.flatMap { account ->
            createTransaction(account, request)
        }.then()

    private fun createTransaction(account: Account, request: TransactionRequest): Mono<Void> {
        return transactionRepository.save(mapToTransaction(account, request)).then()
    }

    private fun hasAvailableBalance(account: Account, requestedTransaction: TransactionRequest): Mono<Account> {
        val hasAvailableLimit: (TransactionRequest, Int) -> Boolean =
            { request: TransactionRequest, currentBalance: Int ->
                if (request.type == TransactionType.DEBIT) (currentBalance - request.amount) >= -account.limit
                else true
            }

        return balanceService.getBalance(account.id)
            .flatMap { currentBalance ->
                if (hasAvailableLimit(requestedTransaction, currentBalance)) Mono.just(account)
                else Mono.error(NotEnoughBalance())
            }
    }

    private fun mapToTransaction(account: Account, request: TransactionRequest): Transaction = Transaction(
        accountId = account.id,
        amount = request.amount,
        type = request.type,
        description = request.description
    )
}
