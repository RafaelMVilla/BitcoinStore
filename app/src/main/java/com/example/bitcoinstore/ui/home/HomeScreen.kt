package com.example.bitcoinstore.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bitcoinstore.R
import com.example.bitcoinstore.ui.components.AppTopBar
import com.example.bitcoinstore.ui.user.UserViewModel
import com.example.bitcoinstore.util.CurrencyUtils

data class Product(val id: Int, val name: String, val priceBRL: Double, val image: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userEmail: String,
    onProductClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit,
    onWalletClick: () -> Unit,
    cartVm: CartViewModel,
    userVm: UserViewModel
) {
    val cartState by cartVm.ui.collectAsState()
    val userState by userVm.ui.collectAsState()

    LaunchedEffect(userEmail) {
        userVm.load(userEmail)
        cartVm.loadCart()
    }

    var query by remember { mutableStateOf("") }

    val products = listOf(
        Product(1, "Carteira de Bitcoin", 499.00, R.drawable.prod1),
        Product(2, "Camisa Bitcoin", 149.00, R.drawable.prod2),
        Product(3, "Boné Crypto", 89.90, R.drawable.prod3),
        Product(4, "Mousepad Blockchain", 69.90, R.drawable.prod4)
    )

    val filtered = remember(query, products) {
        if (query.isBlank()) products
        else products.filter { it.name.contains(query, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "BitcoinStore",
                showBack = false,
                showProfile = true,
                onProfileClick = onProfileClick,
                itemCount = cartState.itemCount,
                onCartClick = onCartClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onWalletClick, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Filled.AccountBalanceWallet, contentDescription = "Carteira", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            Text(
                text = "Bem-vindo, ${userState.name.ifBlank { "usuário" }} 👋",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = { Text("Pesquisar produtos") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filtered) { product ->
                    val btc = CurrencyUtils.brlToBtc(product.priceBRL)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onProductClick(product.id) },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = product.image),
                                contentDescription = product.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .height(120.dp)
                                    .fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(product.name, fontWeight = FontWeight.Bold)
                            Text(
                                text = "${CurrencyUtils.formatBRL(product.priceBRL)}  •  ${CurrencyUtils.formatBTC(btc)}",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
