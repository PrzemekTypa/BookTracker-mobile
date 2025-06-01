package com.example.booktrackermobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.booktrackermobile.navigation.MainNavGraph
import com.example.booktrackermobile.ui.theme.BookTrackerMobileTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            BookTrackerMobileTheme {
                val navController = rememberNavController()
                MainNavGraph(navController = navController)
            }
        }
    }
}
