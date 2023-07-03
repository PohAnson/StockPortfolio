package com.example.owlio.utils

import com.example.owlio.model.Broker
import com.example.owlio.model.TradeType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.ZonedDateTime
import java.util.Date

object DateStringSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Date) =
        encoder.encodeString(TransactionTypeConverters.fromDateToIsoDateString(value))

    override fun deserialize(decoder: Decoder): Date =
        TransactionTypeConverters.fromIsoDateStringToDate(decoder.decodeString())
}

object BrokerStringSerializer : KSerializer<Broker> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Broker", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Broker) =
        encoder.encodeString(TransactionTypeConverters.fromBrokerToString(value))

    override fun deserialize(decoder: Decoder): Broker =
        TransactionTypeConverters.fromStringToBroker(decoder.decodeString())

}


object TradeTypeStringSerializer : KSerializer<TradeType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("TradeType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: TradeType) =
        encoder.encodeString(TransactionTypeConverters.fromTradeTypeToString(value))

    override fun deserialize(decoder: Decoder): TradeType =
        TransactionTypeConverters.fromStringToTradeType(decoder.decodeString())
}


object ZonedDtStringSerializer : KSerializer<ZonedDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ZonedDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ZonedDateTime) =
        encoder.encodeString(TransactionTypeConverters.fromZonedDtToIsoInstantString(value))

    override fun deserialize(decoder: Decoder): ZonedDateTime =
        TransactionTypeConverters.fromIsoInstantStringToZonedDt(decoder.decodeString())
}