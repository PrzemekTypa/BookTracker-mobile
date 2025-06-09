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
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLibraryTab(navController: NavController) {
    val context = LocalContext.current
    val storage = BookStorage(context)
    var books by remember { mutableStateOf<List<Book>>(emptyList()) }

    // Wczytanie książek
    LaunchedEffect(Unit) {
        books = storage.getBooks()
    }

    val statusOptions = listOf("all", "want_to_read", "reading", "read")
    var selectedStatus by remember { mutableStateOf("all") }
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text("Moja biblioteka", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown do filtrowania
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = when (selectedStatus) {
                    "want_to_read" -> "Chcę przeczytać"
                    "reading" -> "Czytam"
                    "read" -> "Przeczytane"
                    else -> "Wszystkie"
                },
                onValueChange = {},
                readOnly = true,
                label = { Text("Filtruj po statusie") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                statusOptions.forEach { status ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                when (status) {
                                    "want_to_read" -> "Chcę przeczytać"
                                    "reading" -> "Czytam"
                                    "read" -> "Przeczytane"
                                    else -> "Wszystkie"
                                }
                            )
                        },
                        onClick = {
                            selectedStatus = status
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filtrowanie książek
        val filteredBooks = books.filter {
            selectedStatus == "all" || it.status == selectedStatus
        }

        if (filteredBooks.isEmpty()) {
            Text("Brak książek w wybranym statusie.")
        } else {
            LazyColumn {
                items(filteredBooks) { book ->
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
                            books = storage.getBooks() // odświeżenie listy
                        }
                    )
                }
            }
        }
    }
}
