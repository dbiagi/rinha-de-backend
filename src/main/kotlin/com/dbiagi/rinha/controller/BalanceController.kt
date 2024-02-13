package com.dbiagi.rinha.controller

import com.dbiagi.rinha.domain.DetailedBalance
import com.dbiagi.rinha.service.BalanceService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class BalanceController(
    private val balanceService: BalanceService
) {
    @GetMapping("clientes/{accountId}/extrato")
    fun getBalance(@PathVariable accountId: Int): Mono<DetailedBalance> = balanceService.getDetailedBalance(accountId)
}
