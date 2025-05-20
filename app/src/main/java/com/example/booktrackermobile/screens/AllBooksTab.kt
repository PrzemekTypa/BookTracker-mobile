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
    val books = listOf(
        Book(title = "Do moich Rodaków ", author_name = listOf("Jan Paweł II")),
        Book(title = "Zbrodnia i kara", author_name = listOf("Fiodor Dostojewski")),
        Book(title = "Koń z Valony", author_name = listOf("Patryk Olszowski"))


    )

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(books) { book ->
            BookItem(book = book)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
