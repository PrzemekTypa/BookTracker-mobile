package com.example.booktrackermobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktrackermobile.model.Book
import com.example.booktrackermobile.model.BooksResponse
import com.example.booktrackermobile.network.RetrofitInstance
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*

class AllBooksViewModel : ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }

    fun searchBooks() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response: BooksResponse = RetrofitInstance.api.searchBooks(_query.value)
                _books.value = response.docs
            } catch (e: Exception) {
                _error.value = when {
                    e.message?.contains("Unable to resolve host", ignoreCase = true) == true ->
                        "Brak połączenia z internetem. Sprawdź połączenie i spróbuj ponownie."
                    else -> "Wystąpił błąd: ${e.localizedMessage ?: "Nieznany błąd"}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}
