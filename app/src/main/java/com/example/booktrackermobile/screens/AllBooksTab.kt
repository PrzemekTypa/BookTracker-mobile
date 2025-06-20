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
import com.example.booktrackermobile.viewmodel.AllBooksViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.booktrackermobile.storage.BookStorage



@Composable
fun AllBooksTab(
    navController: NavHostController,
    viewModel: AllBooksViewModel = viewModel()
) {
    val context = LocalContext.current
    val storage = remember { BookStorage(context) }

    val query by viewModel.query.collectAsState()
    val books by viewModel.books.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Lista książek w bibliotece
    var libraryBooks by remember { mutableStateOf(storage.getBooks()) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        OutlinedTextField(
            value = query,
            onValueChange = { viewModel.updateQuery(it) },
            label = { Text("Wyszukaj książki") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.searchBooks() }) {
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
                            BookItem(
                                book = book,
                                onClick = {
                                    val key = book.key?.removePrefix("/works/")
                                    if (key != null) {
                                        navController.navigate("bookDetails/$key?source=allBooks")
                                    }
                                },
                                isInLibrary = libraryBooks.any { it.key == book.key },
                                onAddClick = {
                                    storage.addBook(book)
                                    libraryBooks = storage.getBooks()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


