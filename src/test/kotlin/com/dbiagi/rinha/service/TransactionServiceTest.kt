package com.dbiagi.rinha.service

import com.dbiagi.rinha.domain.TransactionRequest
import com.dbiagi.rinha.domain.TransactionResponse
import com.dbiagi.rinha.domain.TransactionType
import com.dbiagi.rinha.domain.exception.NotEnoughBalance
import com.dbiagi.rinha.model.Account
import com.dbiagi.rinha.model.Transaction
import com.dbiagi.rinha.repository.TransactionRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.LocalDateTime

class TransactionServiceTest {
    private val balanceService: BalanceService = mock()
    private val transactionRepository: TransactionRepository = mock()
    private val accountService: AccountService = mock()

    private val transactionService: TransactionService =
        TransactionService(accountService, transactionRepository, balanceService)

    @Test
    fun `given a credit transaction request should create a transaction`() {
        // given
        val accountId = 1
        val amount = 100
        val request = TransactionRequest(amount, TransactionType.CREDIT, "Credit transaction")
        val account = Account(id = accountId, limit = 1000, createdAt = LocalDateTime.now())
        val transaction = Transaction(
            accountId = accountId,
            amount = amount,
            type = TransactionType.CREDIT,
            description = "Credit transaction"
        )

        whenever(accountService.getAccount(accountId)).thenReturn(Mono.just(account))
        whenever(balanceService.getBalance(accountId)).thenReturn(Mono.just(0))
        whenever(transactionRepository.save(any())).thenReturn(Mono.just(transaction))

        // then
        StepVerifier.create(transactionService.create(accountId, request))
            .expectNextMatches { result: TransactionResponse ->
                result.limit == account.limit && result.balance == request.amount
            }
            .verifyComplete()

        verify(transactionRepository, atLeastOnce()).save(any())
    }

    @Test
    fun `given a debit function that excedes the limit should throw an exception`() {
        // given
        val accountId = 1
        val request = TransactionRequest(1001, TransactionType.DEBIT, "Debit transaction")
        val account = Account(id = accountId, limit = 1000, createdAt = LocalDateTime.now())

        whenever(accountService.getAccount(accountId)).thenReturn(Mono.just(account))
        whenever(balanceService.getBalance(accountId)).thenReturn(Mono.just(0))

        // then
        StepVerifier.create(transactionService.create(accountId, request))
            .expectError(NotEnoughBalance::class.java)
            .verify()

        verify(transactionRepository, never()).save(any())
    }

    @Test
    fun `given a debit function that dont excedes the limit should create a transaction`() {
        // given
        val accountId = 1
        val request = TransactionRequest(1000, TransactionType.DEBIT, "Debit transaction")
        val account = Account(id = accountId, limit = 1000, createdAt = LocalDateTime.now())

        whenever(accountService.getAccount(accountId)).thenReturn(Mono.just(account))
        whenever(balanceService.getBalance(accountId)).thenReturn(Mono.just(0))
        whenever(transactionRepository.save(any())).thenReturn(Mono.empty())

        // then
        StepVerifier.create(transactionService.create(accountId, request))
            .verifyComplete()

        verify(transactionRepository, atLeastOnce()).save(any())
    }
}
