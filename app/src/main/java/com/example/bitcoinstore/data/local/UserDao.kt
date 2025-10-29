
package com.example.bitcoinstore.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    @Query("UPDATE users SET name = :newName WHERE email = :email")
    suspend fun updateName(email: String, newName: String)

    @Query("UPDATE users SET passwordHash = :newHash WHERE email = :email")
    suspend fun updatePasswordHash(email: String, newHash: String)
}
