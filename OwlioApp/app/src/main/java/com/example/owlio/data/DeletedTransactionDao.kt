package com.example.owlio.data

import androidx.room.Dao
import androidx.room.Query
import com.example.owlio.model.DeletedTransaction

@Dao
interface DeletedTransactionDao {
    @Query("SELECT * FROM `deleted_transaction`")
    suspend fun getAll(): List<DeletedTransaction>

    @Query("SELECT * FROM `deleted_transaction` WHERE `last_modified`>:dateTime")
    suspend fun getDeletedTransactionAfter(dateTime: String): List<DeletedTransaction>
}


