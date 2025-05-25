package com.example.booktrackermobile.network


import com.example.booktrackermobile.model.DescriptionDeserializer
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://openlibrary.org/"

    private val gson = GsonBuilder()
        .registerTypeAdapter(String::class.java, DescriptionDeserializer())
        .create()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}