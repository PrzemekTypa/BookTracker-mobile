package com.example.booktrackermobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.booktrackermobile.screens.*
import com.google.firebase.auth.FirebaseAuth

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

        composable("main") {
            MainScreen(navController)
        }

        composable("bookDetails/{bookKey}") { backStackEntry ->
            val bookKey = backStackEntry.arguments?.getString("bookKey") ?: ""
            BookDetailsScreen(bookKey = bookKey, navController = navController)
        }
    }
}
