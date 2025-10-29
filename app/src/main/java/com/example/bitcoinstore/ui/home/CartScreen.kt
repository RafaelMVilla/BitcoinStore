package com.example.bitcoinstore.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bitcoinstore.ui.components.AppTopBar
import com.example.bitcoinstore.ui.theme.BitcoinOrange
import com.example.bitcoinstore.ui.theme.White
import com.example.bitcoinstore.util.CurrencyUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(cartVm: CartViewModel, onBack: () -> Unit, onWalletClick: () -> Unit) {
    val state by cartVm.ui.collectAsState()
    LaunchedEffect(Unit) { cartVm.loadCart() }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "BitcoinStore",
                showBack = true,
                onBack = onBack,
                itemCount = state.itemCount,
                onCartClick = { /* já estamos no carrinho */ }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onWalletClick, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Filled.AccountBalanceWallet, contentDescription = "Carteira", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        if (state.items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Seu carrinho está vazio!")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.items.size) { index ->
                        val item = state.items[index]
                        val itemBTC = CurrencyUtils.brlToBtc(item.price)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = item.image),
                                    contentDescription = item.name,
                                    modifier = Modifier.size(80.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.name, fontWeight = FontWeight.Bold)
                                    Text("${CurrencyUtils.formatBRL(item.price)}  •  ${CurrencyUtils.formatBTC(itemBTC)}")
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = { cartVm.decreaseQuantity(item) }) {
                                            Icon(Icons.Filled.Remove, contentDescription = "Diminuir")
                                        }
                                        Text("${item.quantity}")
                                        IconButton(onClick = { cartVm.increaseQuantity(item) }) {
                                            Icon(Icons.Filled.Add, contentDescription = "Aumentar")
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        IconButton(onClick = { cartVm.deleteItem(item) }) {
                                            Icon(
                                                Icons.Filled.Delete,
                                                contentDescription = "Remover",
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                val totalBTC = CurrencyUtils.brlToBtc(state.total)
                Text(
                    "Total: ${CurrencyUtils.formatBRL(state.total)}  •  ${CurrencyUtils.formatBTC(totalBTC)}",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* TODO: Finalizar compra */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = BitcoinOrange)
                ) {
                    Text("Finalizar Compra", color = White)
                }
            }
        }
    }
}
