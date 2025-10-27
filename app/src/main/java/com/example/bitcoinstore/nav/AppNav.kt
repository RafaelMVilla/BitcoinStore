package com.example.bitcoinstore.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.bitcoinstore.ui.auth.AuthScreen
import com.example.bitcoinstore.ui.auth.AuthViewModel
import com.example.bitcoinstore.ui.home.HomeScreen
import com.example.bitcoinstore.ui.home.ProductDetailScreen
import com.example.bitcoinstore.ui.home.CartScreen
import com.example.bitcoinstore.ui.home.CartViewModel

sealed class Route(val path: String) {
    data object Auth : Route("auth")
    data object Home : Route("home/{userName}") {
        fun build(userName: String) = "home/$userName"
    }
    data object ProductDetail : Route("product/{productId}") {
        fun build(productId: Int) = "product/$productId"
    }
    data object Cart : Route("cart")
}

@Composable
fun AppNav(authVm: AuthViewModel, cartVm: CartViewModel) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Route.Auth.path) {
        composable(Route.Auth.path) {
            AuthScreen(vm = authVm) { name ->
                nav.navigate(Route.Home.build(name)) {
                    popUpTo(Route.Auth.path) { inclusive = true }
                }
            }
        }
        composable(
            route = Route.Home.path,
            arguments = listOf(navArgument("userName") { type = NavType.StringType })
        ) { backStack ->
            val name = backStack.arguments?.getString("userName").orEmpty()
            HomeScreen(
                userName = name,
                onProductClick = { id -> nav.navigate(Route.ProductDetail.build(id)) },
                onCartClick = { nav.navigate(Route.Cart.path) }
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
            CartScreen(cartVm = cartVm, onBack = { nav.popBackStack() })
        }
    }
}