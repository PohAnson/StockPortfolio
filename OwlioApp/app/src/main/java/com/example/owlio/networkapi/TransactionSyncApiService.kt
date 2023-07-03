package com.example.owlio.networkapi

import com.example.owlio.model.Transaction
import com.example.owlio.utils.ZonedDtStringSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import java.time.ZonedDateTime

interface TransactionSyncApiService {
    @POST("sync/transaction")
    suspend fun syncTransaction(@Body requestBody: RequestBody): SyncTransactionResponse
}

@Serializable
data class SyncTransactionResponse(
    @SerialName("deleted_transaction")
    val deletedTransaction: List<String>,
    @SerialName("modified_transaction")
    val modifiedTransaction: List<Transaction>,
    @SerialName("last_synced_datetime")
    @Serializable(with = ZonedDtStringSerializer::class) val lastSyncedDateTime: ZonedDateTime
)