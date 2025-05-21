package com.example.booktrackermobile.network

import com.example.booktrackermobile.model.BooksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search.json")
    suspend fun searchBooks(@Query("q") query: String): BooksResponse
}