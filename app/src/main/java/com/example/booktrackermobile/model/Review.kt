package com.example.booktrackermobile.model

import java.util.Date


data class Review(
    val bookKey: String,
    val userId: String? = null,
    val rating: Int = 0,
    val reviewText: String = "",
    val timestamp: Date? = null
)
