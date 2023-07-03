package com.example.owlio.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.owlio.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    fun insert(transaction: Transaction)

    @Query("SELECT * FROM `transaction`")
    fun getAllTransaction(): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE transaction_id=:transactionId")
    suspend fun getTransactionById(transactionId: String): Transaction

    @Update
    suspend fun updateTransaction(transaction: Transaction): Int

    @Upsert
    suspend fun upsertTransaction(transaction: Transaction)

    @Query("DELETE FROM `transaction` WHERE transaction_id=:transactionId")
    suspend fun deleteTransaction(transactionId: String)

    @Query("SELECT * FROM `transaction` WHERE last_modified>:dateTime")
    suspend fun getTransactionAfter(dateTime: String): List<Transaction>

    @Query("SELECT `transaction_id` FROM `transaction`")
    suspend fun getAllTransactionId(): List<String>
}