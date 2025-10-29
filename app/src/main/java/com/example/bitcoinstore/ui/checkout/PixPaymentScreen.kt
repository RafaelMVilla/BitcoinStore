package com.example.bitcoinstore.ui.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bitcoinstore.ui.components.AppTopBar
import com.example.bitcoinstore.ui.theme.BitcoinOrange
import com.example.bitcoinstore.ui.theme.White
import com.example.bitcoinstore.util.CurrencyUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PixPaymentScreen(
    totalBRL: Double,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    var awaiting by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Pagamento PIX",
                showBack = true,
                onBack = onBack,
                itemCount = 0,
                onCartClick = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Valor a pagar: ${CurrencyUtils.formatBRL(totalBRL)}", style = MaterialTheme.typography.titleLarge)

            if (awaiting) {
                Text("Aguardando pagamento…")
                Button(
                    onClick = { awaiting = false },
                    colors = ButtonDefaults.buttonColors(containerColor = BitcoinOrange)
                ) { Text("PIX realizado", color = White) }
            } else {
                Text("Pagamento com sucesso, obrigado pela compra!", color = MaterialTheme.colorScheme.tertiary)
                Button(onClick = onSuccess) { Text("Concluir") }
            }
        }
    }
}
