package com.example.booktrackermobile.model


data class WorkDetails(
    val title: String,
    val description: String?,
    val subjects: List<String>?,
    val covers: List<Int>?
)
