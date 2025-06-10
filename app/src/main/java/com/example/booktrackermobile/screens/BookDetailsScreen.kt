package com.example.booktrackermobile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import com.example.booktrackermobile.model.Review
import com.example.booktrackermobile.repository.ReviewRepository
import com.google.firebase.auth.FirebaseAuth
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(bookKey: String, navController: NavController, source: String?) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val storage = BookStorage(context)
    var bookDetail by remember { mutableStateOf<WorkDetails?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var isInLibrary by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    var progress by remember { mutableStateOf(0f) }
    val reviewRepository = remember { ReviewRepository() }
    var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }
    var reviewsLoading by remember { mutableStateOf(true) }


    LaunchedEffect(bookKey) {
        scope.launch {
            try {
                // Pobierz szczegóły książki
                val response = RetrofitInstance.api.getWorkDetails(bookKey)
                bookDetail = response

                // Sprawdź, czy książka już jest w bibliotece
                val allBooks = storage.getBooks()
                isInLibrary = allBooks.any { it.key == "/works/$bookKey" }

            } catch (e: Exception) {
                error = e.message
            }

            // Pobierz recenzje z Firestore
            reviewsLoading = true
            try {
                reviews = reviewRepository.getReviewsForBook("/works/$bookKey")
            } catch (e: Exception) {
                reviews = emptyList()
            }
            reviewsLoading = false
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Szczegóły książki") },
                navigationIcon = {
                    IconButton(onClick = {

                        navController.navigate("main/${source ?: "allBooks"}") {
                            popUpTo("main/allBooks") { inclusive = true }
                        }

                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Powrót")

                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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

                        Spacer(modifier = Modifier.height(24.dp))

                        val storedBook = storage.getBooks().find { it.key == "/works/$bookKey" }
                        val simplifiedBook = Book(
                            title = bookDetail!!.title,
                            author_name = storedBook?.author_name ?: listOf("Brak danych"),
                            first_publish_year = storedBook?.first_publish_year,
                            cover_i = bookDetail!!.covers?.firstOrNull(),
                            key = "/works/$bookKey",
                            note = storedBook?.note,
                            status = storedBook?.status ?: "none",
                            progress = storedBook?.progress ?: 0
                        )

                        Button(
                            onClick = {
                                if (isInLibrary) {
                                    storage.removeBook("/works/$bookKey")
                                    isInLibrary = false
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Usunięto z biblioteki")
                                    }
                                } else {
                                    storage.addBook(simplifiedBook)
                                    isInLibrary = true
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Dodano do biblioteki")
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isInLibrary)
                                    MaterialTheme.colorScheme.error
                                else
                                    MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(if (isInLibrary) "Usuń z mojej biblioteki" else "Dodaj do moich książek")
                        }

                        // Dropdown statusu
                        if (isInLibrary) {
                            Spacer(modifier = Modifier.height(16.dp))

                            var expanded by remember { mutableStateOf(false) }
                            val statusOptions = listOf("none", "want_to_read", "reading", "read")
                            var selectedStatus by remember { mutableStateOf(simplifiedBook.status) }

                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded },
                            ) {
                                TextField(
                                    value = when (selectedStatus) {
                                        "want_to_read" -> "Chcę przeczytać"
                                        "reading" -> "Czytam"
                                        "read" -> "Przeczytane"
                                        else -> "Brak"
                                    },
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Status") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
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
                                                        "none" -> "Brak"
                                                        "want_to_read" -> "Chcę przeczytać"
                                                        "reading" -> "Czytam"
                                                        "read" -> "Przeczytane"
                                                        else -> status
                                                    }
                                                )
                                            },
                                            onClick = {
                                                selectedStatus = status
                                                expanded = false

                                                val updatedBook = simplifiedBook.copy(status = selectedStatus)
                                                storage.updateBook(updatedBook)
                                                scope.launch {
                                                    snackbarHostState.showSnackbar("Zaktualizowano status")
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }



                        var note by remember { mutableStateOf("") }

// Jeśli książka już w bibliotece, załaduj notatkę
                        LaunchedEffect(isInLibrary) {
                            if (isInLibrary) {
                                val stored = storage.getBooks().find { it.key == "/works/$bookKey" }
                                note = stored?.note ?: ""
                                progress = (stored?.progress ?: 0).toFloat()
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (isInLibrary) {
                            OutlinedTextField(
                                value = note,
                                onValueChange = { note = it },
                                label = { Text("Notatka") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    val updatedBook = simplifiedBook.copy(note = note)
                                    storage.updateBook(updatedBook)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Zapisano notatkę")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Zapisz notatkę")
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Postęp czytania: ${progress.toInt()}%", style = MaterialTheme.typography.bodyMedium)

                            Slider(
                                value = progress,
                                onValueChange = { progress = it },
                                valueRange = 0f..100f,
                                steps = 9,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    val updatedBook = simplifiedBook.copy(progress = progress.toInt())
                                    storage.updateBook(updatedBook)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Zapisano postęp")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Zapisz postęp")
                            }

                            // --------------- RECENZJA I OCENA ---------------
                            Spacer(modifier = Modifier.height(16.dp))

                            var reviewText by remember { mutableStateOf("") }
                            var rating by remember { mutableStateOf(0f) }

                            Text("Twoja ocena:")
                            Slider(
                                value = rating,
                                onValueChange = { rating = it },
                                steps = 3,
                                valueRange = 1f..5f,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = reviewText,
                                onValueChange = { reviewText = it },
                                label = { Text("Twoja recenzja") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    scope.launch {
                                        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "anon"
                                        val review = Review(
                                            bookKey = "/works/$bookKey",
                                            userId = userId,
                                            rating = rating.toInt(),
                                            reviewText = reviewText,
                                            timestamp = Date()
                                        )

                                        val success = reviewRepository.addReview(review)
                                        if (success) {
                                            reviews = reviewRepository.getReviewsForBook("/works/$bookKey") // odśwież recenzje
                                            reviewText = ""
                                            rating = 0f
                                        }
                                        snackbarHostState.showSnackbar(
                                            if (success) "Recenzja zapisana!" else "Błąd zapisu recenzji"
                                        )

                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Wyślij recenzję")
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                            Text("Recenzje innych użytkowników:", style = MaterialTheme.typography.titleMedium)

                            if (reviewsLoading) {
                                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                            } else if (reviews.isEmpty()) {
                                Text("Brak recenzji.", style = MaterialTheme.typography.bodyMedium)
                            } else {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    reviews.forEach { review ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            elevation = CardDefaults.cardElevation(2.dp)
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Text("Ocena: ${review.rating}/5", style = MaterialTheme.typography.bodyLarge)
                                                Text(review.reviewText, style = MaterialTheme.typography.bodyMedium)

                                                review.timestamp?.let {
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = "Dodano: ${android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", it)}",
                                                        style = MaterialTheme.typography.labelSmall
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}
