package com.dbiagi.rinha.service

import com.dbiagi.rinha.domain.DetailedBalance
import com.dbiagi.rinha.domain.TransactionType
import com.dbiagi.rinha.model.Account
import com.dbiagi.rinha.model.Transaction
import com.dbiagi.rinha.repository.TransactionRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

class BalanceServiceTest {
    private val transactionRepository: TransactionRepository = mock()
    private val accountService: AccountService = mock()
    private val balanceService = BalanceService(transactionRepository, accountService)

    @Test
    fun `given a valid accountId should calculate the balance`() {
        // given
        val accountId = 1
        val transactions = listOf(
            Transaction(1, 100, TransactionType.CREDIT, accountId, "credit"),
            Transaction(1, 100, TransactionType.DEBIT, accountId, "debit"),
        )
        val account = Account(accountId, 1000, LocalDateTime.now())

        whenever(transactionRepository.findAllByAccountId(accountId)).thenReturn(Flux.fromIterable(transactions))
        whenever(accountService.getAccount(accountId)).thenReturn(Mono.just(account))

        // when
        val balance = balanceService.getBalance(accountId).block()

        // then
        assertEquals(0, balance)
    }

    @Test
    fun `given a valid accountId should calculate the detailed balance`() {
        // given
        val accountId = 1
        val accountLimit = 1000
        val transactions = listOf(
            Transaction(1, 100, TransactionType.CREDIT, accountId, "credit"),
            Transaction(1, 100, TransactionType.DEBIT, accountId, "debit"),
        )
        val account = Account(accountId, accountLimit, LocalDateTime.now())

        whenever(transactionRepository.findAllByAccountId(accountId)).thenReturn(Flux.fromIterable(transactions))
        whenever(accountService.getAccount(accountId)).thenReturn(Mono.just(account))

        // when
        val detailedBalance: DetailedBalance? = balanceService.getDetailedBalance(accountId).block()

        // then
        assertEquals(0, detailedBalance?.balance?.total)
        assertEquals(accountLimit, detailedBalance?.balance?.limit)
        assertEquals(2, detailedBalance?.lastTransactions?.size)
    }
}
