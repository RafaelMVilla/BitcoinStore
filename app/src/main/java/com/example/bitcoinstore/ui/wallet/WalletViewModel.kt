package com.example.bitcoinstore.ui.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitcoinstore.data.repo.WalletRepository
import com.example.bitcoinstore.util.CurrencyUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

data class WalletUiState(
    val balanceSats: Long = 0L,
    val loading: Boolean = false,
    val message: String? = null
)

class WalletViewModel(private val repo: WalletRepository) : ViewModel() {

    private val _ui = MutableStateFlow(WalletUiState())
    val ui: StateFlow<WalletUiState> = _ui

    fun load() {
        viewModelScope.launch {
            _ui.value = _ui.value.copy(loading = true)
            val sats = repo.getBalanceSats()
            _ui.value = WalletUiState(balanceSats = sats, loading = false)
        }
    }

    fun setBalanceBTC(btc: BigDecimal) {
        viewModelScope.launch {
            val sats = CurrencyUtils.btcToSats(btc)
            repo.setBalanceSats(sats)
            _ui.value = _ui.value.copy(balanceSats = sats, message = "Saldo atualizado!")
        }
    }

    /** Debita em BTC. Retorna true se pagou, false se saldo insuficiente. */
    suspend fun payWithBTC(amountBtc: BigDecimal): Boolean {
        val amountSats = CurrencyUtils.btcToSats(amountBtc)
        val ok = repo.trySpendSats(amountSats)
        if (ok) {
            // atualiza estado
            val newBal = repo.getBalanceSats()
            _ui.value = _ui.value.copy(balanceSats = newBal, message = "Pagamento em BTC concluído!")
        }
        return ok
    }

    fun clearMessage() {
        _ui.value = _ui.value.copy(message = null)
    }
}
