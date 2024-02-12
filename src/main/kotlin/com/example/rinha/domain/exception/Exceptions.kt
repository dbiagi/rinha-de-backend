package com.example.rinha.domain.exception

class NotEnoughBalance(
    message: String = "Not enough balance"
): BaseException(message = message)
