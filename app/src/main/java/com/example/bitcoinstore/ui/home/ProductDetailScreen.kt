package com.example.bitcoinstore.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.unit.dp
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
    cartVm: CartViewModel
) {
    val state by cartVm.ui.collectAsState()
    LaunchedEffect(Unit) { cartVm.loadCart() }

    val product = remember(productId) { ProductCatalog.get(productId) }
    val priceBtc = CurrencyUtils.brlToBtc(product.priceBRL)

    Scaffold(
        topBar = {
            AppTopBar(
                title = "BitcoinStore",
                showBack = true,
                onBack = onBack,
                itemCount = state.itemCount,
                onCartClick = onCartClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            Image(
                painter = painterResource(id = product.image),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(260.dp)
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Text(product.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)

            Text(
                "${CurrencyUtils.formatBRL(product.priceBRL)}  •  ${CurrencyUtils.formatBTC(priceBtc)}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(8.dp))

            RatingRow(rating = product.rating)
            Spacer(Modifier.height(6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                product.tags.forEach { TagChipSmall(it) }
            }

            Spacer(Modifier.height(12.dp))

            Text("Descrição", style = MaterialTheme.typography.titleMedium)
            Text(product.description, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    val item = CartEntity(
                        productId = product.id,
                        name = product.name,
                        price = product.priceBRL,
                        description = product.description,
                        quantity = 1,
                        image = product.image
                    )
                    cartVm.addOne(item)
                },
                colors = ButtonDefaults.buttonColors(containerColor = BitcoinOrange),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Adicionar ao Carrinho", color = White)
            }
        }
    }
}
