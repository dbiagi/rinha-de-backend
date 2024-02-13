package com.dbiagi.rinha

import com.dbiagi.rinha.model.Account
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class AccountConfig {
    @Bean
    fun accountCache(): MutableMap<Int, Account> {
        return mutableMapOf()
    }
}
