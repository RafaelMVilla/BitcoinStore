package com.example.bitcoinstore.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bitcoinstore.R
import com.example.bitcoinstore.data.local.CartEntity
import com.example.bitcoinstore.ui.components.AppTopBar
import com.example.bitcoinstore.ui.theme.BitcoinOrange
import com.example.bitcoinstore.ui.theme.White
import com.example.bitcoinstore.util.CurrencyUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    onCartClick: () -> Unit,
    onBack: () -> Unit,
    onWalletClick: () -> Unit,
    cartVm: CartViewModel
) {
    val state by cartVm.ui.collectAsState()
    LaunchedEffect(Unit) { cartVm.loadCart() }

    val product = when (productId) {
        1 -> Product(1, "Carteira de Bitcoin", 499.00, R.drawable.prod1)
        2 -> Product(2, "Camisa Bitcoin", 149.00, R.drawable.prod2)
        3 -> Product(3, "Boné Crypto", 89.90, R.drawable.prod3)
        else -> Product(4, "Mousepad Blockchain", 69.90, R.drawable.prod4)
    }

    val btc = CurrencyUtils.brlToBtc(product.priceBRL)

    Scaffold(
        topBar = {
            AppTopBar(
                title = "BitcoinStore",
                showBack = true,
                onBack = onBack,
                itemCount = state.itemCount,
                onCartClick = onCartClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onWalletClick, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Filled.AccountBalanceWallet, contentDescription = "Carteira", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = product.image),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(product.name, style = MaterialTheme.typography.titleLarge)
            Text(
                text = "${CurrencyUtils.formatBRL(product.priceBRL)}  •  ${CurrencyUtils.formatBTC(btc)}",
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Descrição: Produto de alta qualidade para amantes de criptomoedas!")
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    val item = CartEntity(
                        productId = product.id,
                        name = product.name,
                        price = product.priceBRL,
                        description = "Produto de alta qualidade para amantes de criptomoedas!",
                        quantity = 1,
                        image = product.image
                    )
                    cartVm.addOne(item)
                },
                colors = ButtonDefaults.buttonColors(containerColor = BitcoinOrange)
            ) {
                Text("Adicionar ao Carrinho", color = White)
            }
        }
    }
}
