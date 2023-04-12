package com.example.owlio.model

import android.util.Log
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

private const val TAG = "Transaction"

data class Transaction(
    val transactionId: String = "0",
    val tradeDate: Date,
    val stockCode: String,
    val broker: Broker,
    val tradeType: TradeType,
    val price: Float = 0f,
    val volume: Int = 0
) {

    companion object {
        fun validateTradeDateString(value: String): Boolean {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            try {

                LocalDate.parse(value, formatter)
            } catch (e: DateTimeParseException) {
                return false
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                return false
            }
            return true

        }

        fun validateStockCodeString(value: String): Boolean {
            return false //TODO("Not yet implemented")
        }

        fun validatePriceString(value: String): Boolean {
            try {
                value.toFloat()
            } catch (e: java.lang.NumberFormatException) {
                return false
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                return false
            }
            return true
        }

        fun validateVolumeString(value: String): Boolean {
            try {
                value.toInt()
            } catch (e: java.lang.NumberFormatException) {
                return false
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                return false
            }
            return true
        }
    }
}
