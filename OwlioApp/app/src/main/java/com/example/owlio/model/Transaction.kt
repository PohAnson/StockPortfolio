package com.example.owlio.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.owlio.data.StockInfoRepo
import com.example.owlio.utils.BrokerStringSerializer
import com.example.owlio.utils.DateStringSerializer
import com.example.owlio.utils.TradeTypeStringSerializer
import com.example.owlio.utils.TransactionTypeConverters
import com.example.owlio.utils.ZonedDtStringSerializer
import com.example.owlio.utils.toDate
import com.example.owlio.utils.toLocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import timber.log.Timber
import java.time.Instant
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date


@Entity(
    tableName = "transaction", foreignKeys = [ForeignKey(
        entity = StockInfo::class, childColumns = ["stock_code"], parentColumns = ["trading_code"]
    )]
)
@TypeConverters(TransactionTypeConverters::class)
@Serializable
data class Transaction(
    @PrimaryKey @ColumnInfo(name = "transaction_id") @SerialName("transaction_id")
    var transactionId: String = "",
    @ColumnInfo(name = "trade_date") @SerialName("trade_date") @Serializable(with = DateStringSerializer::class)
    val tradeDate: Date,
    @ColumnInfo(name = "stock_code", index = true) @SerialName("stock_code")
    val stockCode: String,
    @Serializable(with = BrokerStringSerializer::class)
    val broker: Broker,
    @ColumnInfo(
        name = "trade_type",
        typeAffinity = 2
    ) @SerialName("trade_type") @Serializable(with = TradeTypeStringSerializer::class)
    val tradeType: TradeType,
    val price: Float = 0f,
    val volume: Int = 0,
    @ColumnInfo(name = "last_modified") @SerialName("last_modified") @Serializable(with = ZonedDtStringSerializer::class)
    val lastModified: ZonedDateTime = ZonedDateTime.now(),
) {
    init {
        // dynamically generate the timestamp id when missing transaction id
        if (transactionId == "") transactionId = Instant.now().epochSecond.toString()
    }

    fun calculateFees(): Float {
        return broker.calculateFees(value = price * volume, date = tradeDate.toLocalDate())
    }

    companion object {
        private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        fun fromTradeDateString(value: String): Date {
            return LocalDate.parse(value, dateTimeFormatter).toDate()
        }

        fun toTradeDateString(value: Date): String {
            return value.toLocalDate().format(dateTimeFormatter)
        }

        fun validateTradeDateString(value: String): Boolean {
            try {
                fromTradeDateString(value)
            } catch (e: DateTimeParseException) {
                return false
            } catch (e: Exception) {
                Timber.e(e)
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
                Timber.e(e)
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
                Timber.e(e)
                return false
            }
            return true
        }
    }
}

