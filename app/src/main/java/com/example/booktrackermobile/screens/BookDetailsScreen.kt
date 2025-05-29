package com.example.booktrackermobile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.booktrackermobile.model.WorkDetails
import com.example.booktrackermobile.network.RetrofitInstance
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import com.example.booktrackermobile.model.Book
import com.example.booktrackermobile.storage.BookStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(bookKey: String, navController: NavController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val storage = BookStorage(context)
    var bookDetail by remember { mutableStateOf<WorkDetails?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(bookKey) {
        scope.launch {
            try {
                val response = RetrofitInstance.api.getWorkDetails(bookKey)
                bookDetail = response
            } catch (e: Exception) {
                error = e.message
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Szczegóły książki") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Powrót")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when {
                error != null -> Text("Błąd: $error", modifier = Modifier.padding(16.dp))
                bookDetail == null -> CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Okładka książki
                        bookDetail?.covers?.firstOrNull()?.let { coverId ->
                            AsyncImage(
                                model = "https://covers.openlibrary.org/b/id/$coverId-L.jpg",
                                contentDescription = "Okładka książki",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp)
                                    .padding(bottom = 16.dp)
                            )
                        }

                        Text(
                            text = bookDetail!!.title,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = bookDetail!!.description ?: "Brak opisu",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        bookDetail!!.subjects?.let { subjects ->
                            Text(
                                text = "Tematy: ${subjects.joinToString()}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                val simplifiedBook = Book(
                                    title = bookDetail!!.title,
                                    author_name = listOf("Brak danych"),
                                    first_publish_year = null,
                                    cover_i = bookDetail!!.covers?.firstOrNull(),
                                    key = "/works/$bookKey"
                                )
                                storage.addBook(simplifiedBook)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Dodaj do moich książek")
                        }
                    }
                }
            }
        }
    }
}
