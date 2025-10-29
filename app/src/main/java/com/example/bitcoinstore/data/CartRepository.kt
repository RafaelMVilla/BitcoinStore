package com.example.bitcoinstore.data

import com.example.bitcoinstore.data.local.CartDao
import com.example.bitcoinstore.data.local.CartEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepository(private val dao: CartDao) {

    suspend fun addOne(item: CartEntity) = withContext(Dispatchers.IO) {
        val existing = dao.getByProductId(item.productId)
        if (existing != null) {
            dao.updateQuantityBy(item.productId, 1)
        } else {
            dao.insert(item.copy(quantity = 1))
        }
    }

    suspend fun updateQuantity(productId: Int, delta: Int) = withContext(Dispatchers.IO) {
        val existing = dao.getByProductId(productId) ?: return@withContext
        val newQty = existing.quantity + delta
        if (newQty <= 0) {
            dao.delete(existing)
        } else {
            dao.updateQuantityBy(productId, delta)
        }
    }

    suspend fun deleteItem(item: CartEntity) = withContext(Dispatchers.IO) {
        dao.delete(item)
    }

    suspend fun getCart(): List<CartEntity> = withContext(Dispatchers.IO) {
        dao.getAll()
    }

    suspend fun clearCart() = withContext(Dispatchers.IO) {
        dao.clear()
    }
}
