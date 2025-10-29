package com.example.bitcoinstore.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun CartScreen(
    cartVm: CartViewModel,
    onBack: () -> Unit,
    onCheckout: () -> Unit
) {
    val state by cartVm.ui.collectAsState()
    LaunchedEffect(Unit) { cartVm.loadCart() }

    val totalBRL = state.total
    val totalBTC = CurrencyUtils.brlToBtc(totalBRL)

    Scaffold(
        topBar = {
            AppTopBar(
                title = "BitcoinStore",
                showBack = true,
                onBack = onBack,
                itemCount = state.itemCount,
                onCartClick = { /* estamos no carrinho */ }
            )
        },

        bottomBar = {
            if (state.items.isNotEmpty()) {
                Surface(tonalElevation = 3.dp) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .imePadding()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Total", style = MaterialTheme.typography.labelLarge)
                                Text(
                                    text = "${CurrencyUtils.formatBRL(totalBRL)}  •  ${CurrencyUtils.formatBTC(totalBTC)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Button(
                                onClick = onCheckout,
                                colors = ButtonDefaults.buttonColors(containerColor = BitcoinOrange)
                            ) {
                                Text("Finalizar compra", color = White)
                            }
                        }
                    }
                }
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(
                    start = 16.dp, end = 16.dp, top = 16.dp,
                    bottom = 120.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.items, key = { it.id }) { item ->
                    ElevatedCard {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = item.image),
                                contentDescription = item.name,
                                modifier = Modifier.size(72.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.name, fontWeight = FontWeight.SemiBold)
                                val itemBtc = CurrencyUtils.brlToBtc(item.price)
                                Text(
                                    text = "${CurrencyUtils.formatBRL(item.price)}  •  ${CurrencyUtils.formatBTC(itemBtc)}",
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { cartVm.decreaseQuantity(item) }) {
                                        Icon(Icons.Filled.Remove, contentDescription = "Diminuir")
                                    }
                                    Text("${item.quantity}")
                                    IconButton(onClick = { cartVm.increaseQuantity(item) }) {
                                        Icon(Icons.Filled.Add, contentDescription = "Aumentar")
                                    }
                                    Spacer(Modifier.width(8.dp))
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
        }
    }
}
