package com.example.rinha.service

import com.example.rinha.model.Account
import com.example.rinha.repository.AccountRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AccountService(
    val accountRepository: AccountRepository,
    val accountCache: MutableMap<Int, Account>
) {
    val logger = mu.KotlinLogging.logger {}

    fun getAccount(id: Int): Mono<Account> {
        if (isAccountOutOfRange(id)) {
            logger.error("Account id out of range, id=$id")
            return Mono.empty()
        }

        return accountRepository.findById(id)
            .doOnNext {
                if (!accountCache.containsKey(id)) {
                    accountCache[id] = it
                }
            }
    }

    private fun isAccountOutOfRange(id: Int): Boolean = id < 1 || id > 5
}
