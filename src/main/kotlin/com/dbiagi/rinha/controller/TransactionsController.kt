package com.dbiagi.rinha.controller

import com.dbiagi.rinha.domain.TransactionRequest
import com.dbiagi.rinha.domain.TransactionResponse
import com.dbiagi.rinha.service.TransactionService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers.boundedElastic
import java.util.concurrent.CompletableFuture

@RestController
class TransactionsController(
    private val trasactionService: TransactionService
) {
    val logger = LoggerFactory.getLogger(TransactionsController::class.java)

    @PostMapping("/clientes/{accountId}/transacoes")
    fun create(@PathVariable("accountId") accountId: Int, @RequestBody transactionRequest: TransactionRequest): Mono<TransactionResponse> {
        logger.info("Creating transaction for account $accountId, request=$transactionRequest")
        return trasactionService.create(accountId, transactionRequest)
    }

    @GetMapping("/test")
    fun test() = Mono.defer {
        delay().subscribeOn(boundedElastic())

        Mono.just("ok")
    }

    fun delay(): Mono<Void> = Mono.fromRunnable {
        logger.info("start sleep")
        Thread.sleep(1000)
        logger.info("end sleep")
    }
}
