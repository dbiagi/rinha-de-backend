package com.example.rinha.repository

import com.example.rinha.model.Account
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface AccountRepository : R2dbcRepository<Account, Int>{
}
