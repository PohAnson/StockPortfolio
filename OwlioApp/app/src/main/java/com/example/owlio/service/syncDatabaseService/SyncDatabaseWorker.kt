package com.example.owlio.service.syncDatabaseService

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.owlio.data.DeletedTransactionRepo
import com.example.owlio.data.OwlioDataStorePreferences
import com.example.owlio.data.TransactionRepo
import com.example.owlio.data.UserCredentialRepo
import com.example.owlio.networkapi.ApiResult
import com.example.owlio.networkapi.TransactionSyncApiService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import okhttp3.MediaType
import okhttp3.RequestBody
import java.time.format.DateTimeFormatter

@HiltWorker
class SyncDatabaseWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val userCredentialRepo: UserCredentialRepo,
    private val transactionRepo: TransactionRepo,
    private val deletedTransactionRepo: DeletedTransactionRepo,
    private val transactionSyncApiService: TransactionSyncApiService,
    private val owlioDataStorePreferences: OwlioDataStorePreferences,
) : CoroutineWorker(appContext, workerParameters) {


    override suspend fun doWork(): Result {
        // check if connection is established by attempting to
        // validate the sessionId linked to the user
        val checkUserSessionResult = userCredentialRepo.checkUserSession()
        if (checkUserSessionResult is ApiResult.ApiSuccess<*>) {
            if (checkUserSessionResult.data !is Boolean || !checkUserSessionResult.data) {
                // TODO: implement back to login page when failure
                return Result.failure()  // does not have any failure handling action
            }
        } else {
//            return Result.retry()
        }
        val lastSyncedDateTime = owlioDataStorePreferences.lastSyncedDateTime.format(
            DateTimeFormatter.ISO_INSTANT
        )
        // gather the deleted transaction
        val deletedTransaction =
            deletedTransactionRepo.getDeletedTransactionAfter(lastSyncedDateTime)

        // gather modified transaction by comparing the last modified date with last synced date
        val modifiedTransaction = transactionRepo.getTransactionModifiedAfter(lastSyncedDateTime)

        // create json request body
        val jsonTransactionDelta = buildJsonObject {
            put(
                "deleted_transaction",
                Json.encodeToJsonElement(deletedTransaction.map {
                    listOf(
                        it.transaction_id,
                        it.last_modified
                    )
                })
            )
            put("modified_transaction", Json.encodeToJsonElement(modifiedTransaction))
            put("last_synced_datetime", Json.encodeToJsonElement(lastSyncedDateTime))
        }

        val requestBody =
            RequestBody.create(MediaType.get("application/json"), jsonTransactionDelta.toString())


        // send all data to server
        val response = transactionSyncApiService.syncTransaction(requestBody)

        // update the deleted transaction
        response.deletedTransaction.forEach {
            transactionRepo.deleteTransaction(it)
        }

        response.modifiedTransaction.forEach { transaction ->
            transactionRepo.upsertTransaction(
                transaction
            )
        }

        // when success
        owlioDataStorePreferences.updateLastSyncedTo(response.lastSyncedDateTime)
        return Result.success()
    }

}