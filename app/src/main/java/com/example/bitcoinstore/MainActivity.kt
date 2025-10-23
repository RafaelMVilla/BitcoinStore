package com.example.bitcoinstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bitcoinstore.data.local.AppDatabase
import com.example.bitcoinstore.data.repo.UserRepository
import com.example.bitcoinstore.nav.AppNav
import com.example.bitcoinstore.ui.auth.AuthViewModel
import com.example.bitcoinstore.ui.theme.BitcoinStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Wiring simples: DB -> Repo -> ViewModel
        val db = AppDatabase.get(this)
        val repo = UserRepository(db.userDao())

        setContent {
            val authVm: AuthViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return AuthViewModel(repo) as T
                    }
                }
            )

            BitcoinStoreTheme {
                AppNav(authVm)
            }
        }
    }
}
