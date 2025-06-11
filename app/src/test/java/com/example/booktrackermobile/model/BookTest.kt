package com.example.booktrackermobile.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BookTest {

    @Test
    fun book_initialization_works() {
        val book = Book(
            title = "Clean Code",
            author_name = listOf("Robert C. Martin"),
            first_publish_year = 2008,
            cover_i = 123456,
            key = "/books/OL1M",
            note = "Great book",
            status = "reading",
            progress = 50
        )

        assertEquals("Clean Code", book.title)
        assertEquals(listOf("Robert C. Martin"), book.author_name)
        assertEquals(2008, book.first_publish_year)
        assertEquals(123456, book.cover_i)
        assertEquals("/books/OL1M", book.key)
        assertEquals("Great book", book.note)
        assertEquals("reading", book.status)
        assertEquals(50, book.progress)
    }

    @Test
    fun book_default_values_work() {
        val book = Book(
            title = null,
            author_name = null,
            first_publish_year = null,
            cover_i = null,
            key = null
        )

        assertNull(book.title)
        assertNull(book.author_name)
        assertNull(book.first_publish_year)
        assertNull(book.cover_i)
        assertNull(book.key)
        assertNull(book.note) // default null
        assertEquals("none", book.status) // default "none"
        assertEquals(0, book.progress) // default 0
    }
}
