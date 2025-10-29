package com.example.bitcoinstore.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.bitcoinstore.ui.components.AppTopBar
import com.example.bitcoinstore.ui.user.UserViewModel
import com.example.bitcoinstore.util.CurrencyUtils

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

    val products = remember { ProductCatalog.all() }
    val filtered = remember(query, products) {
        if (query.isBlank()) products
        else products.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.tags.any { t -> t.contains(query, ignoreCase = true) } ||
                    it.description.contains(query, ignoreCase = true)
        }
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
            FloatingActionButton(
                onClick = onWalletClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Filled.AccountBalanceWallet,
                    contentDescription = "Carteira",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
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
                label = { Text("Pesquisar produtos (nome, tag ou descrição)") },
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(filtered) { product ->
                    ProductCard(
                        product = product,
                        onClick = { onProductClick(product.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductCard(product: Product, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = painterResource(product.image),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(Modifier.height(8.dp))

            Text(product.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)

            val btc = CurrencyUtils.brlToBtc(product.priceBRL)
            Text(
                text = "${CurrencyUtils.formatBRL(product.priceBRL)}  •  ${CurrencyUtils.formatBTC(btc)}",
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(6.dp))

            RatingRow(rating = product.rating)

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                product.tags.forEach { TagChipSmall(it) }
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = product.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun TagChip(text: String) {
    AssistChip(
        onClick = { /* sem ação */ },
        label = { Text(text) }
    )
}

@Composable
fun RatingRow(rating: Double) {
    val full = rating.toInt().coerceIn(0, 5)
    val hasHalf = (rating - full) >= 0.5 && full < 5
    val empty = 5 - full - if (hasHalf) 1 else 0

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(full) { Icon(Icons.Filled.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
        if (hasHalf) { Icon(Icons.Outlined.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
        repeat(empty) { Icon(Icons.Outlined.Star, contentDescription = null) }
        Spacer(Modifier.width(4.dp))
        Text(String.format("%.1f", rating), style = MaterialTheme.typography.bodySmall)
    }
}
