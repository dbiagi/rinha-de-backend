package com.dbiagi.rinha.domain.exception

class NotEnoughBalance(
    message: String = "Not enough balance"
): BaseException(message = message)
