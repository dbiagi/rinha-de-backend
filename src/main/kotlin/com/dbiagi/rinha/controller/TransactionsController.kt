package com.dbiagi.rinha.controller

import com.dbiagi.rinha.domain.TransactionRequest
import com.dbiagi.rinha.service.TransactionService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class TransactionsController(
    private val trasactionService: TransactionService
) {
    val logger = LoggerFactory.getLogger(TransactionsController::class.java)

    @PostMapping("/clientes/{accountId}/transacoes")
    fun create(@PathVariable("accountId") accountId: Int, @RequestBody transactionRequest: TransactionRequest): Mono<Void> {
        logger.info("Creating transaction for account $accountId, request=$transactionRequest")
        return trasactionService.create(accountId, transactionRequest)
    }
}
