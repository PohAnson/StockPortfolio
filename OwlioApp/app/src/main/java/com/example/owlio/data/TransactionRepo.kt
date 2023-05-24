package com.example.owlio.data

import com.example.owlio.model.Transaction
import javax.inject.Inject

class TransactionRepo @Inject constructor(private val transactionDao: TransactionDao) {
    fun insertTransaction(transaction: Transaction) = transactionDao.insert(transaction)
    fun getAllTransaction() = transactionDao.getAllTransaction()
    suspend fun getTransactionById(transactionId: Int) =
        transactionDao.getTransactionById(transactionId)

    suspend fun updateTransaction(transaction: Transaction) =
        transactionDao.updateTransaction(transaction)
}