package com.example.owlio.model

import java.time.LocalDate
import kotlin.math.max

sealed class Broker(val name: String) {
    abstract fun calculateFees(value: Float, date: LocalDate): Float

    fun calculateSgx(value: Float): Float {
        val clearing = (0.0325 / 100 * value).toFloat()
        val tradingAccess = (0.0075 / 100 * value).toFloat()
        return clearing + tradingAccess
    }

    fun calculateTax(value: Float, date: LocalDate): Float {
        val taxRatePercentage = if (date.isBefore(LocalDate.of(2023, 1, 1))) 7 else 8
        val tax = taxRatePercentage / 100.0 * value
        return tax.toFloat()
    }


    object Poems : Broker("poems") {
        override fun calculateFees(value: Float, date: LocalDate): Float {
            val commission = max(25.0f, 0.28f / 100 * value)
            val settlementInstruction = 0.35f
            val subSum = commission + settlementInstruction + calculateSgx(value)
            return calculateTax(subSum, date) + subSum
        }
    }

    object Moomoo : Broker("moomoo") {
        override fun calculateFees(value: Float, date: LocalDate): Float {
            val commission = max(0.99f, 0.03f / 100 * value)
            val platformFee = commission  // calculated in the same way
            val subSum = commission + platformFee + calculateSgx(value)
            return calculateTax(subSum, date) + subSum


        }
    }

    object INVALID : Broker("INVALID") {
        init {
            throw Exception("Invalid Broker")
        }

        override fun calculateFees(value: Float, date: LocalDate): Float {
            throw Exception("Invalid Broker")
        }
    }

    companion object {
        fun brokerFromString(brokerString: String): Broker {
            return when (brokerString.lowercase()) {
                "poems" -> Poems
                "moomoo" -> Moomoo
                else -> {
                    throw Exception("Invalid Broker String '$brokerString' given.")
                }
            }
        }
    }
}