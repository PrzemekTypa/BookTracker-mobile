package com.example.booktrackermobile.model

data class Book(
    val title: String = "",
    val author_name: List<String>? = emptyList(),
    val first_publish_year: Int? = null,
    val cover_i: Int? = null
)