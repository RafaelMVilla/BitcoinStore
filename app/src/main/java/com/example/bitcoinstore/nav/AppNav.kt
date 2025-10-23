package com.example.bitcoinstore.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.bitcoinstore.ui.auth.AuthScreen
import com.example.bitcoinstore.ui.auth.AuthViewModel
import com.example.bitcoinstore.ui.home.HomeScreen

sealed class Route(val path: String) {
    data object Auth : Route("auth")
    data object Home : Route("home/{userName}") {
        fun build(userName: String) = "home/$userName"
    }
}

@Composable
fun AppNav(authVm: AuthViewModel) {
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
            HomeScreen(userName = name)
        }
    }
}
