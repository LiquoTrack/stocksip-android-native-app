package com.liquotrack.stocksip.shared.domain.model

import java.math.BigDecimal

/**
 * The Money value object represents a monetary amount with its associated currency.
 */
data class Money(
    val amount: BigDecimal = BigDecimal.ZERO,
    val currency: String = "USD"
) {

    fun add(otherMoney: Money): Money {
        require(currency.equals(otherMoney.currency, ignoreCase = true)) {
            "Currency codes must match"
        }
        return copy(amount = amount + otherMoney.amount)
    }

    fun subtract(otherMoney: Money): Money {
        require(currency.equals(otherMoney.currency, ignoreCase = true)) {
            "Currency codes must match"
        }
        val result = amount - otherMoney.amount
        require(result >= BigDecimal.ZERO) { "Insufficient amount" }
        return copy(amount = result)
    }

    fun multiply(multiplier: BigDecimal): Money {
        require(multiplier > BigDecimal.ZERO) { "Multiplier must be positive" }
        return copy(amount = amount * multiplier)
    }

    fun multiply(multiplier: Double): Money =
        multiply(BigDecimal.valueOf(multiplier))

    fun toFormattedString(): String = "$amount $currency"

    companion object {
        /**
         * Parse Money from string format "29.99 USD"
         */
        fun fromString(value: String?): Money {
            if (value.isNullOrBlank()) return Money()

            val parts = value.trim().split(" ")
            return when (parts.size) {
                2 -> Money(
                    amount = parts[0].toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    currency = parts[1]
                )
                1 -> Money(
                    amount = parts[0].toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    currency = "USD"
                )
                else -> Money()
            }
        }
    }
}