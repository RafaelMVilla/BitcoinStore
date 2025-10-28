package com.example.bitcoinstore.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitcoinstore.data.CartRepository
import com.example.bitcoinstore.data.local.CartEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CartUiState(
    val items: List<CartEntity> = emptyList(),
    val total: Double = 0.0,
    val itemCount: Int = 0 // soma das quantidades
)

class CartViewModel(private val repo: CartRepository) : ViewModel() {

    private val _ui = MutableStateFlow(CartUiState())
    val ui: StateFlow<CartUiState> = _ui

    fun loadCart() {
        viewModelScope.launch {
            val list = repo.getCart()
            val total = list.sumOf { it.price * it.quantity }
            val count = list.sumOf { it.quantity }
            _ui.value = CartUiState(list, total, count)
        }
    }

    // usado na tela de produto (adiciona 1 unidade)
    fun addOne(item: CartEntity) {
        viewModelScope.launch {
            repo.addOne(item)
            loadCart()
        }
    }

    fun increaseQuantity(item: CartEntity) {
        viewModelScope.launch {
            repo.updateQuantity(item.productId, +1)
            loadCart()
        }
    }

    fun decreaseQuantity(item: CartEntity) {
        viewModelScope.launch {
            repo.updateQuantity(item.productId, -1) // remove ao chegar a 0
            loadCart()
        }
    }

    fun deleteItem(item: CartEntity) {
        viewModelScope.launch {
            repo.deleteItem(item)
            loadCart()
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repo.clearCart()
            loadCart()
        }
    }
}
