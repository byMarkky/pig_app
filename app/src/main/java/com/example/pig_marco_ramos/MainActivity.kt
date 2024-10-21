package com.example.pig_marco_ramos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pig_marco_ramos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        var ronda = 0
        setContentView(binding.root)

        val animDuration: Long = 450

        val players = arrayOf(
            Player("Jugador 1", binding.playerOneText, binding.playerOneHold),
            Player("Jugador 2", binding.playerTwoText,binding.playerTwoHold),
            Player("Jugador 3", binding.playerThreeText,binding.playerThreeHold),
            Player("Jugador 4", binding.playerFourText,binding.playerFourHold))

        for (player in players) {
            player.holdButton.setOnClickListener {
                player.label.text = player.name
            }
        }


    }   // onCreate
}