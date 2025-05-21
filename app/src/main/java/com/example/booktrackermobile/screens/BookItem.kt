package com.example.booktrackermobile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.booktrackermobile.model.Book

@Composable
fun BookItem(book: Book) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = book.title ?: "Brak tytułu", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = book.author_name?.joinToString(", ") ?: "Autor nieznany")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Rok: ${book.first_publish_year ?: "Brak danych"}")
        }
    }
}
