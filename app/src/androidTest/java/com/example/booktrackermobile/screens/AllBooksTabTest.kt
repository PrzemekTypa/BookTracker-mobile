package com.example.booktrackermobile.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import androidx.navigation.compose.rememberNavController
import com.example.booktrackermobile.viewmodel.AllBooksViewModel
import org.junit.Rule
import org.junit.Test

class AllBooksTabTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun allBooksTab_displaysSearchFieldAndButton() {
        composeTestRule.setContent {
            AllBooksTab(navController = rememberNavController(), viewModel = AllBooksViewModel())
        }

        // uruchamia ekran AllBooksTab i sprawdza, czy są na nim widoczne pola tekstowe i przycisk do wyszukiwania książek.
        composeTestRule.onNodeWithText("Wyszukaj książki").assertIsDisplayed()
        composeTestRule.onNodeWithText("Szukaj").assertIsDisplayed()
    }
}
