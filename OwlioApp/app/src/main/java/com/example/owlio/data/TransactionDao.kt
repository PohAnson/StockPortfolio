package com.example.owlio.data

import androidx.room.Dao
import androidx.room.Insert
import com.example.owlio.model.Transaction

@Dao
interface TransactionDao {
    @Insert
    fun insert(transaction: Transaction)
}