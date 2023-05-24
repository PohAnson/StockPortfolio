package com.example.owlio.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.owlio.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    fun insert(transaction: Transaction)

    @Query("SELECT * FROM 'transaction'")
    fun getAllTransaction(): Flow<List<Transaction>>

    @Query("SELECT * FROM 'transaction' WHERE transaction_id=:transactionId")
    suspend fun getTransactionById(transactionId: Int): Transaction

    @Update
    suspend fun updateTransaction(transaction: Transaction): Int

    @Query("DELETE FROM 'transaction' WHERE transaction_id=:transactionId")
    suspend fun deleteTransaction(transactionId: Int)
}