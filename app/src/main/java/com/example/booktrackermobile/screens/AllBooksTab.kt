package com.example.booktrackermobile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.booktrackermobile.model.Book
import com.example.booktrackermobile.model.BooksResponse
import com.example.booktrackermobile.network.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun AllBooksTab() {
    val scope = rememberCoroutineScope()
    var books by remember { mutableStateOf<List<Book>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response: BooksResponse = RetrofitInstance.api.getBooks()
                books = response.docs
            } catch (e: Exception) {
                error = "Błąd podczas pobierania danych: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        error != null -> {
            Text(
                text = error ?: "Nieznany błąd",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.error
            )
        }
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(books) { book ->
                    BookItem(book = book)
                }
            }
        }
    }
}
