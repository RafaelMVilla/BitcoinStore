// app/src/main/java/com/example/bitcoinstore/ui/home/ProductDetailScreen.kt
package com.example.bitcoinstore.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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

    val product = when (productId) {
        1 -> Product(1, "Carteira de Bitcoin", "R$ 499,00", R.drawable.prod1)
        2 -> Product(2, "Camisa Bitcoin", "R$ 149,00", R.drawable.prod2)
        3 -> Product(3, "Boné Crypto", "R$ 89,90", R.drawable.prod3)
        else -> Product(4, "Mousepad Blockchain", "R$ 69,90", R.drawable.prod4)
    }

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
            Text(product.price, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Descrição: Produto de alta qualidade para amantes de criptomoedas!")
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    val price = product.price.replace("R$", "").replace(",", ".").trim().toDouble()
                    val item = CartEntity(
                        productId = product.id,
                        name = product.name,
                        price = price,
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
