// app/src/main/java/com/example/bitcoinstore/ui/wallet/WalletScreen.kt
package com.example.bitcoinstore.ui.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.bitcoinstore.ui.components.AppTopBar
import com.example.bitcoinstore.ui.theme.BitcoinOrange
import com.example.bitcoinstore.ui.theme.White
import com.example.bitcoinstore.util.CurrencyUtils
import java.math.BigDecimal
import java.math.RoundingMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    walletVm: WalletViewModel,
    onBack: () -> Unit
) {
    val state by walletVm.ui.collectAsState()
    LaunchedEffect(Unit) { walletVm.load() }

    var input by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val currentBtc = CurrencyUtils.satsToBtc(state.balanceSats)
    val currentBRL = CurrencyUtils.btcToBRL(currentBtc)

    // Prévia da conversão do que o usuário digitou
    val previewBtc = runCatching {
        if (input.isBlank()) null
        else BigDecimal(input).setScale(8, RoundingMode.DOWN)
    }.getOrNull()
    val previewBRL = previewBtc?.let { CurrencyUtils.btcToBRL(it) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Minha Carteira",
                showBack = true,
                onBack = onBack,
                itemCount = 0,
                onCartClick = onBack
            )
        }
    ) { padding: PaddingValues ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Saldo atual", style = MaterialTheme.typography.titleLarge)
            Text("${CurrencyUtils.formatBTC(currentBtc)} (${CurrencyUtils.formatBRL(currentBRL)})")

            Divider()

            Text("Definir saldo (BTC) — até 8 casas decimais")
            OutlinedTextField(
                value = input,
                onValueChange = { v ->
                    // permite apenas dígitos e ponto; limita a 8 casas decimais
                    val ok = v.matches(Regex("^\\d*(?:\\.\\d{0,8})?$"))
                    if (ok && v.length <= 20) {
                        input = v
                        error = null
                    }
                },
                placeholder = { Text("Ex.: 0.00500000") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier
                    .fillMaxWidth(),
                trailingIcon = {
                    if (input.isNotBlank()) {
                        IconButton(onClick = { input = "" }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Limpar")
                        }
                    }
                },
                isError = error != null,
                supportingText = {
                    when {
                        error != null -> Text(error!!)
                        previewBtc != null -> Text("Prévia: ${CurrencyUtils.formatBTC(previewBtc)} • ${CurrencyUtils.formatBRL(previewBRL!!)}")
                    }
                }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        try {
                            if (input.isBlank()) {
                                error = "Informe um valor em BTC"
                                return@Button
                            }
                            val btc = BigDecimal(input).setScale(8, RoundingMode.DOWN)
                            if (btc < BigDecimal.ZERO) {
                                error = "Valor negativo não permitido"
                                return@Button
                            }
                            walletVm.setBalanceBTC(btc)
                            input = ""
                            error = null
                        } catch (_: Exception) {
                            error = "Valor inválido"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BitcoinOrange)
                ) {
                    Text("Salvar", color = White)
                }

                TextButton(onClick = { input = ""; error = null }) {
                    Text("Cancelar")
                }
            }

            state.message?.let {
                Spacer(Modifier.height(4.dp))
                Text(it, color = MaterialTheme.colorScheme.tertiary)
                LaunchedEffect(it) {
                    kotlinx.coroutines.delay(1500)
                    walletVm.clearMessage()
                }
            }
        }
    }
}
