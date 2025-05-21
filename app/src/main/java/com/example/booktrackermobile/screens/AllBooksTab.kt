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
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var query by remember { mutableStateOf("fantasy") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Wyszukaj książki") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            scope.launch {
                isLoading = true
                error = null
                try {
                    val response: BooksResponse = RetrofitInstance.api.searchBooks(query)
                    books = response.docs
                } catch (e: Exception) {
                    error = "Błąd: ${e.message}"
                } finally {
                    isLoading = false
                }
            }
        }) {
            Text("Szukaj")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> CircularProgressIndicator()
            error != null -> Text(error ?: "", color = MaterialTheme.colorScheme.error)
            else -> {
                Column {
                    Text(
                        text = "Znaleziono ${books.size} wyników",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn {
                        items(books) { book ->
                            BookItem(book = book)
                        }
                    }

                }

            }
        }
    }
}
