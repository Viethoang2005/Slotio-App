package com.example.slotio.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.slotio.ui.auth.LoginScreen
import com.example.slotio.ui.auth.RegisterScreen
import com.example.slotio.ui.home.HomeScreen
import com.example.slotio.ui.home.search.SearchScreen
import com.example.slotio.ui.profile.ProfileScreen


//sealed class Screen(val route: String) {
//    object Login: Screen("login")
//    object Register: Screen("register")
//    object Home: Screen("home")
//    object Booking: Screen("booking")
//}
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.Home
    ) {
        // Login
        composable(Routes.Login) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Login) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.Register)
                }
            )
        }

        // Home
        composable(Routes.Home) {
            HomeScreen(
                onNavigateToSearchScreen = {
                    navController.navigate(Routes.Search)
                }
            )
        }

        // Register
        composable(Routes.Register) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.Login) {
                        popUpTo(Routes.Register) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Routes.Login)
                }
            )
        }

        // Profile
        composable(Routes.Profile) {
            ProfileScreen()
        }

        composable(Routes.Search) {
            SearchScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

