package com.example.pig_marco_ramos

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.pig_marco_ramos.databinding.ActivityWinnerBinding

class WinnerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWinnerBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWinnerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nPlayers = intent.getIntExtra("PLAYER_NUMBER", 2)
        val players: MutableList<String> = mutableListOf()
        val scores: MutableList<Int> = mutableListOf()

        for (i in 0..<nPlayers) {
            val name: String = intent.getStringExtra("PLAYER_" + (i + 1) + "_NAME").toString()
            val score = intent.getIntExtra("PLAYER_" + (i + 1) + "_SCORE", 0)
            println("NAME: $name")
            println("SCORE: $score")
        }

        binding.winner.text = players[0]
        binding.winner.text = players[1]
        binding.winner.text = players[2]
        binding.winner.text = players[3]

    }
}