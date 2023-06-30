package com.example.owlio.data

import kotlinx.serialization.Serializable
import javax.inject.Inject

class DeletedTransactionRepo @Inject constructor(private val deletedTransactionDao: DeletedTransactionDao) {
    suspend fun getAllDeletedTransaction(): List<DeletedTransaction> {
        return deletedTransactionDao.getAllDeletedTransactionId()
    }
}

@Serializable
data class DeletedTransaction(val transaction_id: String, val last_modified: String)