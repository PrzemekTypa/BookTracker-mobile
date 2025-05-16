package com.example.booktrackermobile.model

data class Book(
    val title: String,
    val author_name: List<String>?,
    val first_publish_year: Int?,
    val cover_i: Int?
)