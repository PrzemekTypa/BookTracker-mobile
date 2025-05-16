package com.example.booktrackermobile.network

import com.example.booktrackermobile.model.BookResponse
import retrofit2.http.GET

interface ApiService {
    @GET("books")
    suspend fun getBooks(): BookResponse
}