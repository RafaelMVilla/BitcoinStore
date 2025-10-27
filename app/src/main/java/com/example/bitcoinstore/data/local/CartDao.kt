package com.example.bitcoinstore.data.local

import androidx.room.*

@Dao
interface CartDao {

    @Query("SELECT * FROM cart")
    suspend fun getAll(): List<CartEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartEntity)

    @Update
    suspend fun update(item: CartEntity)

    @Delete
    suspend fun delete(item: CartEntity)

    @Query("DELETE FROM cart")
    suspend fun clear()
}