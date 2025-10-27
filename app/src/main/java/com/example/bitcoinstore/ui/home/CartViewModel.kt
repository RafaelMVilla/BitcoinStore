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
    val total: Double = 0.0
)

class CartViewModel(private val repo: CartRepository) : ViewModel() {

    private val _ui = MutableStateFlow(CartUiState())
    val ui: StateFlow<CartUiState> = _ui

    fun loadCart() {
        viewModelScope.launch {
            val list = repo.getCart()
            val total = list.sumOf { it.price * it.quantity }
            _ui.value = CartUiState(list, total)
        }
    }

    fun addToCart(item: CartEntity) {
        viewModelScope.launch {
            repo.addToCart(item)
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