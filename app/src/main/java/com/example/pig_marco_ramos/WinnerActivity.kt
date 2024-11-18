package com.example.pig_marco_ramos

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.TextView
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
        val players: MutableList<PlayerDClass?> = mutableListOf()
        val labels: MutableList<TextView> = mutableListOf(binding.winner, binding.second, binding.third, binding.four)

        for (i in 0..<nPlayers) {
            val player: PlayerDClass? = intent.getParcelableExtra("PLAYER_" + (i + 1), PlayerDClass::class.java)
            players.add(player)
        }

        val sorted = players.sortedByDescending { it?.totalPoints }

        for (i in 0..<nPlayers) {
            println(sorted[i]?.name + " " + sorted[i]?.totalPoints)
            labels[i].text = "" + (i + 1) + ". " + sorted[i]?.name + " - Points: " + sorted[i]?.totalPoints
        }



    }
}