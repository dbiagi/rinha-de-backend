package com.example.rinha.domain.exception

class NotEnoughBalance(
    message: String = "Not enough balance for this account."
): UnprocessableException(errors = listOf(AppError("not_enough_balance", message)))
