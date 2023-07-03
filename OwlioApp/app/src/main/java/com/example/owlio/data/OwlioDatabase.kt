package com.example.owlio.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.owlio.model.DeletedTransaction
import com.example.owlio.model.StockInfo
import com.example.owlio.model.Transaction

@Database(
    entities = [StockInfo::class, Transaction::class, DeletedTransaction::class],
    version = 1,
    exportSchema = false
)
abstract class OwlioDatabase : RoomDatabase() {
    abstract fun stockInfoDao(): StockInfoDao
    abstract fun transactionDao(): TransactionDao
    abstract fun deletedTransactionDao(): DeletedTransactionDao

    companion object {
        @Volatile
        private var INSTANCE: OwlioDatabase? = null

        fun getDatabase(context: Context): OwlioDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext, OwlioDatabase::class.java, "OwlioDatabase"
                ).fallbackToDestructiveMigration().createFromAsset("databases/prepopulated_data.db")
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            db.execSQL(
                                """
                                CREATE TRIGGER update_time_trigger BEFORE UPDATE OF 
                                trade_date, stock_code, broker, trade_type, price, volume ON "transaction" 
                                    FOR EACH ROW
                                BEGIN
                                    UPDATE "transaction" SET last_modified = datetime()
                                    WHERE transaction_id = OLD.transaction_id;
                                END;
                            """.trimMargin()
                            )
                            db.execSQL(
                                """
                                    CREATE TABLE IF NOT EXISTS deleted_transaction(
                                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                                        transaction_id TEXT,
                                        last_modified TEXT
                                    )
                                """.trimMargin()
                            )
                            db.execSQL(
                                """
                                    CREATE TRIGGER delete_transaction_trigger
                                    BEFORE DELETE ON "transaction" FOR EACH ROW
                                    BEGIN
                                        INSERT INTO "deleted_transaction"(transaction_id, last_modified) 
                                        VALUES(
                                            OLD.transaction_id, 
                                            strftime('%Y-%m-%dT%H:%M:%fZ', 'now')
                                        );
                                    END;
                                """.trimMargin()
                            )
                        }
                    }).build().also {
                        INSTANCE = it
                    }
            }
        }
    }

}