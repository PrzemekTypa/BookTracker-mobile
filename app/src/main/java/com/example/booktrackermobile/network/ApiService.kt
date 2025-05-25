package com.example.booktrackermobile.network

import com.example.booktrackermobile.model.BooksResponse
import com.example.booktrackermobile.model.WorkDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search.json")
    suspend fun searchBooks(@Query("q") query: String): BooksResponse

    @GET("/works/{workKey}.json")
    suspend fun getWorkDetails(@Path("workKey") key: String): WorkDetails
}