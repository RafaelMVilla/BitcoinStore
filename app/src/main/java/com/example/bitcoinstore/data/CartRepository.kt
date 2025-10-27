package com.example.bitcoinstore.data

import com.example.bitcoinstore.data.local.CartDao
import com.example.bitcoinstore.data.local.CartEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepository(private val dao: CartDao) {

    suspend fun addToCart(item: CartEntity) = withContext(Dispatchers.IO) {
        val existing = dao.getAll().find { it.productId == item.productId }
        if (existing != null) {
            val updated = existing.copy(quantity = existing.quantity + item.quantity)
            dao.update(updated)
        } else {
            dao.insert(item)
        }
    }

    suspend fun getCart(): List<CartEntity> = withContext(Dispatchers.IO) {
        dao.getAll()
    }

    suspend fun clearCart() = withContext(Dispatchers.IO) {
        dao.clear()
    }
}