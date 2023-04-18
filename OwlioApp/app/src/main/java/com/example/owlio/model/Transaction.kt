package com.example.owlio.model

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.owlio.data.StockInfoRepo
import com.example.owlio.utils.toDate
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date

private const val TAG = "Transaction"


@Entity(
    tableName = "transaction", foreignKeys = [ForeignKey(
        entity = StockInfo::class, childColumns = ["stock_code"], parentColumns = ["trading_code"]
    )]
)
@TypeConverters(TransactionTypeConverters::class)
data class Transaction(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "transaction_id") val transactionId: Int = 0,
    @ColumnInfo(name = "trade_date") val tradeDate: Date,
    @ColumnInfo(name = "stock_code", index = true) val stockCode: String,
    val broker: Broker,
    @ColumnInfo(name = "trade_type", typeAffinity = 2) val tradeType: TradeType,
    val price: Float = 0f,
    val volume: Int = 0,
) {


    companion object {
        fun fromTradeDateString(value: String): Date {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            return LocalDate.parse(value, formatter).toDate()
        }

        fun validateTradeDateString(value: String): Boolean {
            try {
                fromTradeDateString(value)
            } catch (e: DateTimeParseException) {
                return false
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                return false
            }
            return true

        }

        suspend fun validateStockCodeString(
            stockCode: String, stockInfoRepo: StockInfoRepo
        ): Boolean {
            return stockInfoRepo.getStockInfoByCode(stockCode).isNotEmpty()
        }

        fun priceStringToFloat(value: String): Float {
            return value.toFloat()
        }

        fun validatePriceString(value: String): Boolean {
            try {
                priceStringToFloat(value)
            } catch (e: java.lang.NumberFormatException) {
                return false
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                return false
            }
            return true
        }

        fun volumeStringToInt(value: String): Int {
            return value.toInt()

        }

        fun validateVolumeString(value: String): Boolean {
            try {
                volumeStringToInt(value)
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


class TransactionTypeConverters {
    @TypeConverter
    fun fromDateToIsoDateString(date: Date): String {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(
            DateTimeFormatter.ISO_DATE
        )
    }

    @TypeConverter
    fun fromIsoDateStringToDate(isoString: String): Date {
        return LocalDate.parse(isoString, DateTimeFormatter.ISO_DATE).toDate()
    }

    @TypeConverter
    fun fromBrokerToString(broker: Broker): String {
        return broker.name
    }

    @TypeConverter
    fun fromStringToBroker(value: String): Broker {
        return Broker.brokerFromString(value)
    }

    @TypeConverter
    fun fromTradeTypeToString(tt: TradeType): String {
        return tt.name
    }

    @TypeConverter
    fun fromStringToTradeType(value: String): TradeType {
        return tradeTypeFromString(value)
    }

}
