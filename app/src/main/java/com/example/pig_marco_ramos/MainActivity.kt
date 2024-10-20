package com.example.pig_marco_ramos

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pig_marco_ramos.databinding.ActivityMainBinding
import kotlin.properties.Delegates
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        var ronda = 0
        setContentView(binding.root)

        binding.button.setOnClickListener {
            if (ronda >= 5) {
                binding.text.text = "Se acabo el juego"
            } else {
                binding.text.text = (Random.nextInt(0, 6) + 1).toString()
                ronda += 1
            }

        }

    }
}