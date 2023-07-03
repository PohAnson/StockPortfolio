package com.example.owlio.utils

import androidx.room.TypeConverter
import com.example.owlio.model.Broker
import com.example.owlio.model.TradeType
import com.example.owlio.model.tradeTypeFromString
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date


object TransactionTypeConverters {
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
        return tt.name.lowercase()
    }

    @TypeConverter
    fun fromStringToTradeType(value: String): TradeType {
        return tradeTypeFromString(value)
    }

    @TypeConverter
    fun fromZonedDtToIsoInstantString(value: ZonedDateTime): String {
        return value.format(DateTimeFormatter.ISO_INSTANT)
    }

    @TypeConverter
    fun fromIsoInstantStringToZonedDt(value: String): ZonedDateTime {
        return ZonedDateTime.parse(
            value, DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault())
        )
    }
}
