package com.example.pig_marco_ramos.games.chuck

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pig_marco_ramos.api.chuck.Service
import com.example.pig_marco_ramos.api.chuck.model.JokeResponse
import com.example.pig_marco_ramos.databinding.ActivityChuckBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChuckJokes: AppCompatActivity() {

    lateinit var binding: ActivityChuckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityChuckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CustomAdapter(emptyList()) {}

        binding.recyclerView.adapter = adapter

        loadCategories()

    }

    private fun loadJoke(category: String) {
        val retrofit = Retrofit.Builder().baseUrl("https://api.chucknorris.io/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(Service::class.java)
        val call = service.getJoke(category)

        call.enqueue(object : Callback<JokeResponse> {
            override fun onResponse(call: Call<JokeResponse>, response: Response<JokeResponse>) {
                if (response.isSuccessful) {
                    Log.i("RESPONSE", "${response.body()}")
                    binding.textJoke.text = response.body()?.value ?: "There is no joke :("
                } else {
                    Log.e("RESPONSE", "RESPONSE ERROR")
                }
            }

            override fun onFailure(call: Call<JokeResponse>, t: Throwable) {
                Log.e("CALL:joke", "ERROR: {}", t)
            }
        })
    }

    private fun loadCategories() {
        val retrofit = Retrofit.Builder().baseUrl("https://api.chucknorris.io/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(Service::class.java)
        val call = service.getCategories()

        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    // Set the categories to the recyclerView
                    val adapter = CustomAdapter(dataSet = response.body()) { category ->
                        loadJoke(category)
                    }

                    binding.recyclerView.adapter = adapter
                } else {
                    Log.e("RESPONSE", "RESPONSE ERROR")
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

}