package com.example.booktrackermobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.booktrackermobile.navigation.MainNavGraph
import com.example.booktrackermobile.screens.LoginScreen
import com.example.booktrackermobile.screens.MainScreen
import com.example.booktrackermobile.ui.theme.BookTrackerMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookTrackerMobileTheme {
                var isLoggedIn by remember { mutableStateOf(false) }

                if (isLoggedIn) {
                    val navController = rememberNavController()
                    MainNavGraph(navController = navController)
                } else {
                    LoginScreen(onLoginSuccess = { isLoggedIn = true })
                }
            }
        }
    }
}
