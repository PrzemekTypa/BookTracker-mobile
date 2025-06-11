package com.example.booktrackermobile.viewmodel

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

// sprawdza, czy metoda updateQuery w ViewModelu faktycznie zmienia stan zapytania na nową wartość.

class AllBooksViewModelTest {

    @Test
    fun updateQuery_updatesQueryState() = runBlocking {
        val viewModel = AllBooksViewModel()

        viewModel.updateQuery("kotlin")

        // Pobierz aktualną wartość query z flow
        val queryValue = viewModel.query.first()

        assertEquals("kotlin", queryValue)
    }
}
