package com.example.booktrackermobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.booktrackermobile.screens.MainScreen
import com.example.booktrackermobile.screens.BookDetailsScreen

@Composable
fun MainNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController)
        }
        composable("bookDetails/{bookKey}") { backStackEntry ->
            val bookKey = backStackEntry.arguments?.getString("bookKey") ?: ""
            BookDetailsScreen(bookKey = bookKey, navController = navController)
        }
    }
}