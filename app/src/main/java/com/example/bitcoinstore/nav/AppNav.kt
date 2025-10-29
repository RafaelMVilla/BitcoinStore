package com.example.bitcoinstore.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bitcoinstore.ui.auth.AuthScreen
import com.example.bitcoinstore.ui.auth.AuthViewModel
import com.example.bitcoinstore.ui.checkout.CheckoutScreen
import com.example.bitcoinstore.ui.checkout.PayMethod
import com.example.bitcoinstore.ui.home.CartScreen
import com.example.bitcoinstore.ui.home.CartViewModel
import com.example.bitcoinstore.ui.home.HomeScreen
import com.example.bitcoinstore.ui.home.ProductDetailScreen
import com.example.bitcoinstore.ui.user.ProfileScreen
import com.example.bitcoinstore.ui.user.UserViewModel
import com.example.bitcoinstore.ui.wallet.WalletScreen
import com.example.bitcoinstore.ui.wallet.WalletViewModel
import com.example.bitcoinstore.ui.success.SuccessPaymentScreen

sealed class Route(val path: String) {
    data object Auth : Route("auth")
    data object Home : Route("home/{userEmail}") {
        fun build(userEmail: String) = "home/$userEmail"
    }
    data object ProductDetail : Route("product/{productId}") {
        fun build(productId: Int) = "product/$productId"
    }
    data object Cart : Route("cart")
    data object Profile : Route("profile/{userEmail}") {
        fun build(userEmail: String) = "profile/$userEmail"
    }
    data object Wallet : Route("wallet/{userEmail}") {
        fun build(userEmail: String) = "wallet/$userEmail"
    }
    data object Success : Route("success/{method}") {
        fun build(method: String) = "success/$method"
    }
    data object Checkout : Route("checkout")
}

@Composable
fun AppNav(
    authVm: AuthViewModel,
    cartVm: CartViewModel,
    userVm: UserViewModel,
    walletVm: WalletViewModel
) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Route.Auth.path) {

        composable(Route.Auth.path) {
            AuthScreen(vm = authVm) { email ->
                nav.navigate(Route.Home.build(email)) {
                    popUpTo(Route.Auth.path) { inclusive = true }
                }
            }
        }

        composable(
            route = Route.Home.path,
            arguments = listOf(navArgument("userEmail") { type = NavType.StringType })
        ) { backStack ->
            val email = backStack.arguments?.getString("userEmail").orEmpty()
            HomeScreen(
                userEmail = email,
                onProductClick = { id -> nav.navigate(Route.ProductDetail.build(id)) },
                onCartClick = { nav.navigate(Route.Cart.path) },
                onProfileClick = { nav.navigate(Route.Profile.build(email)) },
                onWalletClick = { nav.navigate(Route.Wallet.build(email)) },
                cartVm = cartVm,
                userVm = userVm
            )
        }

        composable(
            route = Route.ProductDetail.path,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStack ->
            val id = backStack.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(
                productId = id,
                onCartClick = { nav.navigate(Route.Cart.path) },
                onBack = { nav.popBackStack() },
                cartVm = cartVm
            )
        }

        composable(Route.Cart.path) {
            CartScreen(
                cartVm = cartVm,
                onBack = { nav.popBackStack() },
                onCheckout = { nav.navigate(Route.Checkout.path) }
            )
        }

        composable(
            route = Route.Profile.path,
            arguments = listOf(navArgument("userEmail") { type = NavType.StringType })
        ) { backStack ->
            val email = backStack.arguments?.getString("userEmail").orEmpty()
            ProfileScreen(
                userEmail = email,
                userVm = userVm,
                cartVm = cartVm,
                onBack = { nav.popBackStack() },
                onLogout = {
                    authVm.logout()
                    cartVm.clearCart()

                    nav.navigate(Route.Auth.path) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onCartClick = { nav.navigate(Route.Cart.path) }
            )
        }

        composable(
            route = Route.Wallet.path,
            arguments = listOf(navArgument("userEmail") { type = NavType.StringType })
        ) {
            WalletScreen(
                walletVm = walletVm,
                onBack = { nav.popBackStack() }
            )
        }

        composable(Route.Checkout.path) {
            CheckoutScreen(
                cartVm = cartVm,
                walletVm = walletVm,
                onBack = { nav.popBackStack() },
                onSuccess = { method: PayMethod ->
                    nav.navigate(Route.Success.build(method.name.lowercase())) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Route.Success.path,
            arguments = listOf(navArgument("method") { type = NavType.StringType })
        ) { backStack ->
            val method = backStack.arguments?.getString("method").orEmpty()
            SuccessPaymentScreen(
                method = method,
                onGoHome = {
                    nav.popBackStack(Route.Home.path, inclusive = false)
                }
            )
        }
    }
}
