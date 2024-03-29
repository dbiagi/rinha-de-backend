package com.dbiagi.rinha.model

import com.dbiagi.rinha.domain.TransactionType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("transactions")
data class Transaction(
    @Id
    var id: Int? = null,
    val amount: Int,

    val type: TransactionType,

    @Column("account_id")
    val accountId: Int,
    val description: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
