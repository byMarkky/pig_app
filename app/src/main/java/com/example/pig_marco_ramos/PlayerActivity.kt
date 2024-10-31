package com.example.pig_marco_ramos

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pig_marco_ramos.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val playerNumber = intent.getIntExtra("PLAYER_NUMBER", 2)
        val roundNumber = intent.getStringExtra("ROUND_NUMBER")

        binding.textView.text = playerNumber.toString()
        binding.textView12.text = roundNumber

    }
}