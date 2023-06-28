package com.example.owlio.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DeletedTransactionDao {
    @Query("SELECT `transaction_id`, `last_modified` FROM `transaction`")
    suspend fun getAllDeletedTransactionId(): List<DeletedTransaction>
}


