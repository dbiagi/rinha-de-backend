package com.dbiagi.rinha.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.time.LocalDateTime

data class Account (
    @Id
    val id: Int,

    @Column("credit_limit")
    val limit: Int,

    val createdAt: LocalDateTime,
)
