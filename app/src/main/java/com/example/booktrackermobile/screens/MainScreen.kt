package com.example.booktrackermobile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen() {
    val tabs = listOf("Wszystkie książki", "Moja biblioteka", "Ustawienia")
    var selectedTabIndex by remember { mutableStateOf(0) }

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
            0 -> AllBooksTab()
            1 -> MyLibraryTab()
            2 -> SettingsTab()
        }
    }
}

@Composable
fun AllBooksTab() {
    Text("Tutaj będą wszystkie książki")
}

@Composable
fun MyLibraryTab() {
    Text("Tutaj będzie Twoja biblioteka")
}

@Composable
fun SettingsTab() {
    Text("Tutaj będą ustawienia")
}
