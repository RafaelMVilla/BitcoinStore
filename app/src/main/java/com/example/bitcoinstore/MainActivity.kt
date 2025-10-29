package com.example.bitcoinstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bitcoinstore.data.repo.CartRepository
import com.example.bitcoinstore.data.local.AppDatabase
import com.example.bitcoinstore.data.repo.UserRepository
import com.example.bitcoinstore.data.repo.WalletRepository
import com.example.bitcoinstore.nav.AppNav
import com.example.bitcoinstore.ui.auth.AuthViewModel
import com.example.bitcoinstore.ui.home.CartViewModel
import com.example.bitcoinstore.ui.theme.BitcoinStoreTheme
import com.example.bitcoinstore.ui.user.UserViewModel
import com.example.bitcoinstore.ui.wallet.WalletViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.get(this)
        val userRepo = UserRepository(db.userDao())
        val cartRepo = CartRepository(db.cartDao())
        val walletRepo = WalletRepository(db.walletDao())

        setContent {
            val authVm: AuthViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return AuthViewModel(userRepo) as T
                    }
                }
            )
            val cartVm: CartViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return CartViewModel(cartRepo) as T
                    }
                }
            )
            val userVm: UserViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return UserViewModel(userRepo) as T
                    }
                }
            )
            val walletVm: WalletViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return WalletViewModel(walletRepo) as T
                    }
                }
            )

            BitcoinStoreTheme {
                AppNav(authVm, cartVm, userVm, walletVm)
            }
        }
    }
}
