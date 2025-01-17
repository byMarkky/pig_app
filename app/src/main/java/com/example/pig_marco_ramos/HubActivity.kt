package com.example.pig_marco_ramos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pig_marco_ramos.games.PIG.MainActivity
import com.example.pig_marco_ramos.databinding.ActivityHubBinding
import com.example.pig_marco_ramos.games.chuck.ChuckJokes

class HubActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pigLaunchBtn.setOnClickListener {
            val intent = Intent(this@HubActivity, MainActivity::class.java)
            startActivity(intent)
        }

        binding.chuckLaunchBtn.setOnClickListener {
            val intent = Intent(this@HubActivity, ChuckJokes::class.java)
            startActivity(intent)
        }

    }

}