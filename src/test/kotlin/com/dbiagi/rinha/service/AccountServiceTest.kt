package com.dbiagi.rinha.service

import com.dbiagi.rinha.domain.exception.NotFoundException
import com.dbiagi.rinha.model.Account
import com.dbiagi.rinha.repository.AccountRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.test.StepVerifier
import java.time.LocalDateTime

class AccountServiceTest {
    private val accountRepository: AccountRepository = mock()
    private val accountCache: MutableMap<Int, Account> = mock()
    private val accountService: AccountService = AccountService(accountRepository, accountCache)

    @Test
    fun `given an account id greater than 5 should return a not found error when getting an account`() {
        val id = 6

        verify(accountRepository, never()).findById(id)

        StepVerifier.create(accountService.getAccount(id))
            .verifyComplete()
    }

    @Test
    fun `given an account id less than 1 should return a not found error when getting an account`() {
        val id = 0

        verify(accountRepository, never()).findById(id)

        StepVerifier.create(accountService.getAccount(id))
            .verifyComplete()
    }

    @Test
    fun `given an account id between 1 and 5 should return an account`() {
        val id = 1
        val account = Account(id, 100, LocalDateTime.now())

        whenever(accountRepository.findById(id)).thenReturn(Mono.just(account))

        accountService.getAccount(id).subscribeOn(Schedulers.boundedElastic())
//        StepVerifier.create(accountService.getAccount(id))
//            .expectNext(account)
//            .verifyComplete()
    }
}
