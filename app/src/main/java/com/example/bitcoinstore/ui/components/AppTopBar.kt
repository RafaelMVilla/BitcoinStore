// app/src/main/java/com/example/bitcoinstore/ui/components/AppTopBar.kt
package com.example.bitcoinstore.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bitcoinstore.ui.theme.BitcoinOrange
import com.example.bitcoinstore.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String = "BitcoinStore",
    showBack: Boolean = false,
    onBack: (() -> Unit)? = null,
    showProfile: Boolean = false,
    onProfileClick: (() -> Unit)? = null,
    itemCount: Int = 0,
    onCartClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(title, color = White) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = BitcoinOrange),
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = { onBack?.invoke() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar", tint = White)
                }
            } else if (showProfile) {
                IconButton(onClick = { onProfileClick?.invoke() }) {
                    Icon(Icons.Filled.Person, contentDescription = "Perfil", tint = White)
                }
            }
        },
        actions = {
            Box {
                IconButton(onClick = onCartClick) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrinho", tint = White)
                }
                if (itemCount > 0) {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            ) { Text("$itemCount") }
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 4.dp, end = 4.dp)
                    ) { /* badge overlay */ }
                }
            }
        }
    )
}
