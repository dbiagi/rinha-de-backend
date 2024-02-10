package com.example.rinha.repository

import com.example.rinha.model.Transaction
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface TransactionRepository : R2dbcRepository<Transaction, Int> {
}
