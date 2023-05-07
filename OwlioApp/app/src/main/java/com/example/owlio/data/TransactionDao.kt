package com.example.owlio.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.owlio.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    fun insert(transaction: Transaction)

    @Query("SELECT * FROM 'transaction'")
    fun getAllTransaction(): Flow<List<Transaction>>
}