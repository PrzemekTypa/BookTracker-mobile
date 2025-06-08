package com.example.booktrackermobile.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.booktrackermobile.model.Book
import coil.compose.AsyncImage
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.example.booktrackermobile.storage.BookStorage


@Composable
fun BookItem(
    book: Book,
    onClick: () -> Unit,
    isInLibrary: Boolean = false,
    onRemoveClick: (() -> Unit)? = null,
    onAddClick: (() -> Unit)? = null
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {

            val imageUrl = book.cover_i?.let { "https://covers.openlibrary.org/b/id/${it}-L.jpg" }

            AsyncImage(
                model = imageUrl,
                contentDescription = "Okładka książki",
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 16.dp)
            )

            
            Column {
                Text(
                    text = book.title ?: "Brak tytułu",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = book.author_name?.joinToString(", ") ?: "Autor nieznany")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Rok: ${book.first_publish_year ?: "Brak danych"}")

                Spacer(modifier = Modifier.height(8.dp))

                if (!isInLibrary) {
                    Button(
                        onClick = { onAddClick?.invoke() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Dodaj do moich książek")
                    }
                }

                onRemoveClick?.let {
                    Button(
                        onClick = it,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Usuń z mojej biblioteki")
                    }
                }
            }
        }
    }
}
