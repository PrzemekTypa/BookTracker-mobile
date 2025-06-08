package com.example.booktrackermobile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.booktrackermobile.viewmodel.AllBooksViewModel
import androidx.lifecycle.viewmodel.compose.viewModel



@Composable
fun MainScreen(navController: NavHostController, selectedTab: String = "allBooks") {

    val allBooksViewModel: AllBooksViewModel = viewModel()
    val tabKeys = listOf("allBooks", "myLibrary", "settings")
    val tabs = listOf("Wszystkie książki", "Moja biblioteka", "Ustawienia")
    var selectedTabIndex by remember {
        mutableStateOf(tabKeys.indexOf(selectedTab).takeIf { it >= 0 } ?: 0)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTabIndex) {
            0 -> AllBooksTab(navController, viewModel = allBooksViewModel)
            1 -> MyLibraryTab(navController)
            2 -> SettingsTab(navController)
        }
    }
}


