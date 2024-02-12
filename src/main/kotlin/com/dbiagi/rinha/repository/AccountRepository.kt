package com.dbiagi.rinha.repository

import com.dbiagi.rinha.model.Account
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface AccountRepository : R2dbcRepository<Account, Int>{
}
