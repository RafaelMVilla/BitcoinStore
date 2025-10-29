package com.example.bitcoinstore.ui.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.bitcoinstore.ui.components.AppTopBar
import com.example.bitcoinstore.ui.home.CartViewModel
import com.example.bitcoinstore.ui.wallet.WalletViewModel
import com.example.bitcoinstore.util.CurrencyUtils
import com.example.bitcoinstore.ui.theme.BitcoinOrange
import com.example.bitcoinstore.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartVm: CartViewModel,
    walletVm: WalletViewModel,
    onBack: () -> Unit,
    onSuccess: (PayMethod) -> Unit   // ✅ novo callback
) {
    val cartState by cartVm.ui.collectAsState()
    val scope = rememberCoroutineScope()

    val totalBRL = cartState.total
    val totalBTC = CurrencyUtils.brlToBtc(totalBRL)

    var method by remember { mutableStateOf(PayMethod.PIX) }
    var pixWaiting by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Checkout",
                showBack = true,
                onBack = onBack,
                itemCount = cartState.itemCount,
                onCartClick = onBack
            )
        },
        bottomBar = {
            if (cartState.items.isNotEmpty()) {
                Surface(tonalElevation = 3.dp) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .imePadding()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            FilterChip(
                                selected = method == PayMethod.PIX,
                                onClick = { method = PayMethod.PIX },
                                label = { Text("Pagar com Pix") }
                            )
                            FilterChip(
                                selected = method == PayMethod.BTC,
                                onClick = { method = PayMethod.BTC },
                                label = { Text("Pagar com ₿ BTC") }
                            )
                        }

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
                                onClick = {
                                    if (method == PayMethod.PIX) {
                                        pixWaiting = true
                                    } else {
                                        scope.launch {
                                            val ok = walletVm.payWithBTC(totalBTC)
                                            if (ok) {
                                                cartVm.clearCart()
                                                onSuccess(PayMethod.BTC)   // ✅ sucesso BTC
                                            } else {
                                                message = "Saldo insuficiente na carteira."
                                            }
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BitcoinOrange)
                            ) {
                                Text(
                                    if (method == PayMethod.PIX) "Gerar Pix" else "Pagar em BTC",
                                    color = White
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, top = 16.dp,
                bottom = 140.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cartState.items, key = { it.id }) { item ->
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
                            val btcUnit = CurrencyUtils.brlToBtc(item.price)
                            Text(
                                text = "${CurrencyUtils.formatBRL(item.price)}  •  ${CurrencyUtils.formatBTC(btcUnit)}",
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(6.dp))
                            Text("Qtd: ${item.quantity}")
                        }
                    }
                }
            }
        }

        // Overlay do PIX: ao confirmar, limpa o carrinho e vai para a tela de sucesso
        if (pixWaiting) {
            Surface(
                tonalElevation = 6.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Aguardando pagamento via Pix…", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Valor: ${CurrencyUtils.formatBRL(totalBRL)}  •  ${CurrencyUtils.formatBTC(totalBTC)}",
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = {
                            pixWaiting = false
                            cartVm.clearCart()
                            onSuccess(PayMethod.PIX)      // ✅ sucesso PIX
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BitcoinOrange)
                    ) {
                        Text("Pix realizado", color = White)
                    }
                    Spacer(Modifier.height(12.dp))
                    TextButton(onClick = { pixWaiting = false }) {
                        Text("Cancelar")
                    }
                }
            }
        }

        // Snackbar
        message?.let {
            LaunchedEffect(it) {
                kotlinx.coroutines.delay(2000)
                message = null
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.BottomCenter
            ) {
                Snackbar(modifier = Modifier.padding(16.dp)) { Text(it) }
            }
        }
    }
}
