package com.example.bitcoinstore.data.repo

import com.example.bitcoinstore.data.local.WalletDao
import com.example.bitcoinstore.data.local.WalletEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class WalletRepository(private val dao: WalletDao) {

    suspend fun getBalanceSats(): Long = withContext(Dispatchers.IO) {
        dao.get()?.balanceSats ?: 0L
    }

    suspend fun setBalanceSats(sats: Long) = withContext(Dispatchers.IO) {
        val current = dao.get() ?: WalletEntity(id = 1, balanceSats = 0L)
        dao.upsert(current.copy(balanceSats = sats))
    }

    /** Tenta debitar; retorna true se conseguiu, false se saldo insuficiente */
    suspend fun trySpendSats(amountSats: Long): Boolean = withContext(Dispatchers.IO) {
        val cur = dao.get() ?: WalletEntity(id = 1, balanceSats = 0L)
        if (cur.balanceSats >= amountSats) {
            dao.upsert(cur.copy(balanceSats = cur.balanceSats - amountSats))
            true
        } else {
            false
        }
    }

    /** Apenas para simular latência de rede, se quiser usar no PIX */
    suspend fun simulateDelay(ms: Long = 800L) = withContext(Dispatchers.IO) { delay(ms) }
}
