package com.example.bitcoinstore.data.local

import androidx.room.*

@Dao
interface CartDao {

    @Query("SELECT * FROM cart")
    suspend fun getAll(): List<CartEntity>

    @Query("SELECT * FROM cart WHERE productId = :productId LIMIT 1")
    suspend fun getByProductId(productId: Int): CartEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartEntity)

    @Update
    suspend fun update(item: CartEntity)

    @Delete
    suspend fun delete(item: CartEntity)

    @Query("UPDATE cart SET quantity = quantity + :delta WHERE productId = :productId")
    suspend fun updateQuantityBy(productId: Int, delta: Int)

    @Query("DELETE FROM cart")
    suspend fun clear()
}
