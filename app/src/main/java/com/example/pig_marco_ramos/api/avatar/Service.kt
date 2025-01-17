package com.example.pig_marco_ramos.api.avatar

import com.example.pig_marco_ramos.api.avatar.model.ResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("api/?inc=gender,picture")
    fun getRandom(@Query("gender") gender: String, @Query("results") results: Int): Call<ResponseData>
}