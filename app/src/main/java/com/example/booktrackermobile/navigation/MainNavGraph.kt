package com.example.booktrackermobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.booktrackermobile.screens.*
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun MainNavGraph(navController: NavHostController) {
    // Czy użytkownik jest zalogowany
    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
    val startDestination = if (isLoggedIn) "main" else "login"

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onNavigateToResetPassword = {
                    navController.navigate("reset")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("reset") {
            ResetPasswordScreen(
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "main?selectedTab={selectedTab}",
            arguments = listOf(
                navArgument("selectedTab") {
                    type = NavType.StringType
                    defaultValue = "allBooks"
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val selectedTab = backStackEntry.arguments?.getString("selectedTab") ?: "allBooks"
            MainScreen(navController, selectedTab)
        }

        composable(
            route = "bookDetails/{bookKey}?source={source}",
            arguments = listOf(
                navArgument("bookKey") { type = NavType.StringType },
                navArgument("source") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = "allBooks"
                }
            )
        ) { backStackEntry ->
            val bookKey = backStackEntry.arguments?.getString("bookKey") ?: ""
            val source = backStackEntry.arguments?.getString("source") ?: "allBooks"
            BookDetailsScreen(bookKey = bookKey, navController = navController, source = source)
        }
    }
}
