package com.example.bitcoinstore.ui.success

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bitcoinstore.ui.components.AppTopBar
import com.example.bitcoinstore.ui.theme.BitcoinOrange
import com.example.bitcoinstore.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessPaymentScreen(
    method: String,
    onGoHome: () -> Unit
) {
    val title = when (method.lowercase()) {
        "pix" -> "Pagamento via PIX confirmado!"
        "btc" -> "Pagamento em Bitcoin confirmado!"
        else -> "Pagamento confirmado!"
    }
    val subtitle = "Obrigado pela compra. Seu pedido foi processado com sucesso."

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Pagamento",
                showBack = false,
                onCartClick = onGoHome,
                itemCount = 0
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(92.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onGoHome,
                colors = ButtonDefaults.buttonColors(containerColor = BitcoinOrange)
            ) {
                Text("Voltar para a Home", color = White)
            }
        }
    }
}
