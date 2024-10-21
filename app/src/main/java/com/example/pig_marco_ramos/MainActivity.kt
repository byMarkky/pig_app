package com.example.pig_marco_ramos

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pig_marco_ramos.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        var ronda = 0
        setContentView(binding.root)

        val animDuration: Long = 450

        binding.imageView.setOnClickListener {
            val random = (Random.nextInt(0, 6) + 1)

            val valueAnimator = ValueAnimator.ofInt(1, random)

            valueAnimator.addUpdateListener {
                binding.textView.text = valueAnimator.animatedValue.toString()
            }

            valueAnimator.setDuration(animDuration)

            if (ronda >= 5) {
                binding.textView.text = "Se acabo el juego"
            } else {
                binding.textView.text = random.toString()
                valueAnimator.start()
                ronda += 1
            }
        }   // onClickListener
    }   // onCreate
}