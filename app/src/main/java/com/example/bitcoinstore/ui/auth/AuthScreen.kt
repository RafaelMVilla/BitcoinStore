// app/src/main/java/com/example/bitcoinstore/ui/auth/AuthScreen.kt
package com.example.bitcoinstore.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen(
    vm: AuthViewModel,
    onLoggedIn: (String) -> Unit // agora passamos o EMAIL para a rota
) {
    val state by vm.ui.collectAsState()

    LaunchedEffect(state.loggedInUser) {
        state.loggedInUser?.let { onLoggedIn(it.email) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (state.isSignUp) "Criar conta" else "Entrar",
            style = MaterialTheme.typography.headlineMedium
        )

        if (state.isSignUp) {
            OutlinedTextField(
                value = state.name,
                onValueChange = vm::updateName,
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        OutlinedTextField(
            value = state.email,
            onValueChange = vm::updateEmail,
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = vm::updatePassword,
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (state.isSignUp) {
            OutlinedTextField(
                value = state.confirm,
                onValueChange = vm::updateConfirm,
                label = { Text("Confirmar senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (state.error != null) {
            val isSuccess = state.error!!.contains("Cadastro realizado", ignoreCase = true)
            Text(
                text = state.error!!,
                color = if (isSuccess) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = vm::submit,
            enabled = !state.loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (state.isSignUp) "Cadastrar" else "Entrar")
        }

        TextButton(onClick = vm::toggleMode, enabled = !state.loading) {
            Text(if (state.isSignUp) "Já tem conta? Entrar" else "Não tem conta? Cadastre-se")
        }

        if (state.loading) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }
    }
}
