package com.example.bitcoinstore.ui.user

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.bitcoinstore.ui.components.AppTopBar
import com.example.bitcoinstore.ui.home.CartViewModel
import com.example.bitcoinstore.ui.theme.BitcoinOrange
import com.example.bitcoinstore.ui.theme.White
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userEmail: String,
    userVm: UserViewModel,
    cartVm: CartViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onCartClick: () -> Unit
) {
    val userState by userVm.ui.collectAsState()
    val cartState by cartVm.ui.collectAsState()
    LaunchedEffect(userEmail) {
        userVm.load(userEmail)
        cartVm.loadCart()
    }

    var editing by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    LaunchedEffect(userState.name) {
        if (!editing) tempName = userState.name
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "BitcoinStore",
                showBack = true,
                onBack = onBack,
                itemCount = cartState.itemCount,
                onCartClick = onCartClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Minha conta", style = MaterialTheme.typography.titleLarge)
            Text("E-mail: ${userState.email}")

            OutlinedTextField(
                value = if (editing) tempName else userState.name,
                onValueChange = { if (editing) tempName = it },
                label = { Text("Nome") },
                enabled = editing,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Nova senha (opcional)") },
                visualTransformation = PasswordVisualTransformation(),
                enabled = editing,
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (!editing) {
                    Button(
                        onClick = { editing = true },
                        colors = ButtonDefaults.buttonColors(containerColor = BitcoinOrange)
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = White)
                        Spacer(Modifier.width(8.dp))
                        Text("Editar", color = White)
                    }
                } else {
                    Button(
                        onClick = {
                            userVm.updateName(tempName)
                            userVm.saveName()
                            if (newPassword.isNotBlank()) {
                                userVm.changePassword(newPassword)
                                newPassword = ""
                            }
                            editing = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BitcoinOrange)
                    ) {
                        Text("Salvar", color = White)
                    }
                    OutlinedButton(onClick = { editing = false; newPassword = ""; tempName = userState.name }) {
                        Text("Cancelar")
                    }
                }
            }

            Divider()

            OutlinedButton(onClick = onLogout) {
                Icon(Icons.Filled.Logout, contentDescription = "Sair")
                Spacer(Modifier.width(8.dp))
                Text("Sair da conta")
            }

            userState.message?.let {
                val isSuccess = !it.contains("erro", ignoreCase = true)
                Text(
                    text = it,
                    color = if (isSuccess) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
                )
                LaunchedEffect(it) {
                    delay(2000)
                    userVm.clearMessage()
                }
            }
        }
    }
}
