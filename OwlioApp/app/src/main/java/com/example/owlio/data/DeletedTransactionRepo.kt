package com.example.owlio.data

import com.example.owlio.model.DeletedTransaction
import javax.inject.Inject

class DeletedTransactionRepo @Inject constructor(private val deletedTransactionDao: DeletedTransactionDao) {
    suspend fun getDeletedTransactionAfter(dateTime: String): List<DeletedTransaction> {
        return deletedTransactionDao.getDeletedTransactionAfter(dateTime)
    }
}

