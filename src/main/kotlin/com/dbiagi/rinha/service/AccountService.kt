package com.dbiagi.rinha.service

import com.dbiagi.rinha.domain.exception.NotFoundException
import com.dbiagi.rinha.model.Account
import com.dbiagi.rinha.repository.AccountRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class AccountService(
    val accountRepository: AccountRepository,
    val accountCache: MutableMap<Int, Account>
) {
    val logger = mu.KotlinLogging.logger {}

    fun getAccount(id: Int): Mono<Account> = Mono.defer {
        if (isAccountOutOfRange(id)) {
            logger.error("Account id out of range, id=$id")
            return@defer Mono.error(NotFoundException("Account id out of range for id=$id"))
        }

        if (accountCache.containsKey(id)) {
            return@defer Mono.just(accountCache[id]!!)
        }

        accountRepository.findById(id)
    }.switchIfEmpty {
        Mono.error(NotFoundException("Account not found for id=$id"))
    }.doOnError { error ->
        logger.error("Error while getting account for id=$id, message=${error.message}", error)
    }.map {
        if (!accountCache.containsKey(it.id)) {
            accountCache[it.id] = it
        }

        it
    }

    fun isAccountOutOfRange(id: Int): Boolean = id < 1 || id > 5
}
