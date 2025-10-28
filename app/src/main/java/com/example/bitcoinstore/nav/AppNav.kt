package com.example.bitcoinstore.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bitcoinstore.ui.auth.AuthScreen
import com.example.bitcoinstore.ui.auth.AuthViewModel
import com.example.bitcoinstore.ui.home.CartScreen
import com.example.bitcoinstore.ui.home.CartViewModel
import com.example.bitcoinstore.ui.home.HomeScreen
import com.example.bitcoinstore.ui.home.ProductDetailScreen
import com.example.bitcoinstore.ui.user.ProfileScreen
import com.example.bitcoinstore.ui.user.UserViewModel

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
}

@Composable
fun AppNav(
    authVm: AuthViewModel,
    cartVm: CartViewModel,
    userVm: UserViewModel
) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Route.Auth.path) {

        composable(Route.Auth.path) {
            AuthScreen(vm = authVm) { email ->
                nav.navigate(Route.Home.build(email)) {
                    popUpTo(Route.Auth.path) { inclusive = true }
                    launchSingleTop = true
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
                onBack = { nav.popBackStack() }
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
                onBack = { nav.popBackStack() },
                onLogout = {
                    // ✅ 1) limpa estado de autenticação
                    authVm.logout()
                    // ✅ 2) volta para Auth e limpa toda a pilha
                    nav.navigate(Route.Auth.path) {
                        popUpTo(nav.graph.findStartDestination().id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
