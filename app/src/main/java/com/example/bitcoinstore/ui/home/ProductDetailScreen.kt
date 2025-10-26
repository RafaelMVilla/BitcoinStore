package com.example.bitcoinstore.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bitcoinstore.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    onCartClick: () -> Unit,
    onBack: () -> Unit
) {
    val product = when (productId) {
        1 -> Product(1, "Carteira de Bitcoin", "R$ 499,00", R.drawable.prod1)
        2 -> Product(2, "Camisa Bitcoin", "R$ 149,00", R.drawable.prod2)
        3 -> Product(3, "Boné Crypto", "R$ 89,90", R.drawable.prod3)
        else -> Product(4, "Mousepad Blockchain", "R$ 69,90", R.drawable.prod4)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BitcoinStore") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = onCartClick) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrinho")
                    }
                }
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
            Button(onClick = { /* Futuro: adicionar ao carrinho */ }) {
                Text("Adicionar ao Carrinho")
            }
        }
    }
}
