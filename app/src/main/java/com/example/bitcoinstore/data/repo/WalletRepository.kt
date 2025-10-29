package com.example.bitcoinstore.data.repo

import com.example.bitcoinstore.data.local.WalletDao
import com.example.bitcoinstore.data.local.WalletEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WalletRepository(private val dao: WalletDao) {

    suspend fun getBalanceSats(): Long = withContext(Dispatchers.IO) {
        dao.get()?.balanceSats ?: 0L
    }

    suspend fun setBalanceSats(sats: Long) = withContext(Dispatchers.IO) {
        val current = dao.get() ?: WalletEntity(id = 1, balanceSats = 0L)
        dao.upsert(current.copy(balanceSats = sats))
    }
}
