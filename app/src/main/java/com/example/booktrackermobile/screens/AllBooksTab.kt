package com.example.booktrackermobile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.booktrackermobile.model.Book
import com.example.booktrackermobile.model.BooksResponse
import com.example.booktrackermobile.network.RetrofitInstance
import kotlinx.coroutines.launch
import androidx.navigation.NavHostController

@Composable
fun AllBooksTab(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    var books by rememberSaveable { mutableStateOf<List<Book>>(emptyList()) }
    var query by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }


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
                    error = when {
                        e.message?.contains("Unable to resolve host", ignoreCase = true) == true ->
                            "Brak połączenia z internetem. Sprawdź połączenie i spróbuj ponownie."
                        else -> "Wystąpił błąd: ${e.localizedMessage ?: "Nieznany błąd"}"
                    }
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
                            BookItem(book = book, onClick = {
                                val key = book.key?.removePrefix("/works/")
                                if (key != null) {
                                    navController.navigate("bookDetails/$key?source=allBooks")
                                }
                            })
                        }
                    }

                }

            }
        }
    }
}
