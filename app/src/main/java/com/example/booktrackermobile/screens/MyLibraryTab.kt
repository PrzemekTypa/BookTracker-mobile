package com.example.booktrackermobile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.booktrackermobile.model.Book
import com.example.booktrackermobile.storage.BookStorage

@Composable
fun MyLibraryTab(navController: NavController) {
    val context = LocalContext.current
    val storage = BookStorage(context)
    var books by remember { mutableStateOf<List<Book>>(emptyList()) }

    LaunchedEffect(Unit) {
        books = storage.getBooks()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Moja biblioteka", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        if (books.isEmpty()) {
            Text("Brak książek w bibliotece.")
        } else {
            LazyColumn {
                items(books) { book ->
                    BookItem(
                        book = book,
                        onClick = {
                            val key = book.key?.removePrefix("/works/")
                            if (key != null) {
                                navController.navigate("bookDetails/$key?source=myLibrary")
                            }
                        },
                        isInLibrary = true,
                        onRemoveClick = {
                            storage.removeBook(book.key ?: "")
                            books = storage.getBooks() // odświeżenie listy po usunięciu
                        }
                    )
                }
            }
        }
    }
}
