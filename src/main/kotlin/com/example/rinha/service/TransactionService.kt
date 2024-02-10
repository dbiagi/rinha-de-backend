package com.example.rinha.service

import com.example.rinha.domain.TransactionRequest
import com.example.rinha.domain.TransactionType
import com.example.rinha.domain.exception.AppError
import com.example.rinha.domain.exception.UnprocessableException
import com.example.rinha.model.Account
import com.example.rinha.model.Transaction
import com.example.rinha.repository.TransactionRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TransactionService(
    private val accountService: AccountService,
    private val transactionRepository: TransactionRepository
) {
    fun create(accountId: Int, request: TransactionRequest): Mono<Void> {
        return Mono.defer {
            if(isValidRequest(request))
                createTransaction(accountId, request)
            else
                Mono.error(UnprocessableException(listOf(AppError("invalid_request", "Invalid request"))))
        }
    }

    fun createTransaction(accountId: Int, request: TransactionRequest): Mono<Void> {
        return accountService.getAccount(accountId)
            .flatMap { account ->
                val transaction = mapToTransaction(account, request)
                transactionRepository.save(transaction)
            }
            .then()
    }

    private fun isValidRequest(request: TransactionRequest): Boolean {
        return request.amount <= 0
    }

    private fun mapToTransaction(account: Account, request: TransactionRequest): Transaction = Transaction(
        accountId = account.id,
        amount = request.amount,
        type = request.type,
        description = request.description
    )
}
