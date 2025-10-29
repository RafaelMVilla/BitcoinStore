package com.example.bitcoinstore.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallet")
data class WalletEntity(
    @PrimaryKey val id: Int = 1,
    val balanceSats: Long = 0L
)
