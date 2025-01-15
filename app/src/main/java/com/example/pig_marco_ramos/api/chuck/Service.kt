package com.example.pig_marco_ramos.api.chuck

import com.example.pig_marco_ramos.api.chuck.model.JokeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("jokes/categories")
    fun getCategories(): Call<List<String>>

    @GET("jokes/random")
    fun getJoke(@Query("category") category: String): Call<JokeResponse>
}