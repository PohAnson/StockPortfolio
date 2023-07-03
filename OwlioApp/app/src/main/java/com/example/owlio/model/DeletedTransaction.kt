package com.example.owlio.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "deleted_transaction")
data class DeletedTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val transaction_id: String,
    val last_modified: String
)
