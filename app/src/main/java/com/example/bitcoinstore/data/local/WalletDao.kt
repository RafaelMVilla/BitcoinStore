package com.example.bitcoinstore.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallet WHERE id = 1")
    suspend fun get(): WalletEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: WalletEntity)
}
