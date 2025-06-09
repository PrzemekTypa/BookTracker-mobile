package com.example.booktrackermobile.storage

import android.content.Context
import com.example.booktrackermobile.model.Book
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BookStorage(context: Context) {

    private val prefs = context.getSharedPreferences("my_books", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val key = "saved_books"

    fun saveBooks(books: List<Book>) {
        val json = gson.toJson(books)
        prefs.edit().putString(key, json).apply()
    }

    fun getBooks(): List<Book> {
        val json = prefs.getString(key, null)
        return if (json != null) {
            val type = object : TypeToken<List<Book>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun addBook(book: Book) {
        val books = getBooks().toMutableList()
        if (books.none { it.key == book.key }) { // bez duplikatow
            books.add(book)
            saveBooks(books)
        }
    }

    fun removeBook(bookKey: String) {
        val books = getBooks().filterNot { it.key == bookKey }
        saveBooks(books)
    }

    fun updateBook(updatedBook: Book) {
        val books = getBooks().toMutableList()
        val index = books.indexOfFirst { it.key == updatedBook.key }
        if (index != -1) {
            books[index] = updatedBook
            saveBooks(books)
        }
    }

}
