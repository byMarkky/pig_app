package com.example.pig_marco_ramos

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Visibility
import com.example.pig_marco_ramos.databinding.ActivityMainBinding
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentPlayer: Player
    private var currentPlayerIndex = 0
    private lateinit var players: Array<Player>
    private var ronda = 1
    private val roundStr = "Round "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO("Implement the startup function")

        binding.startLayout.visibility = View.VISIBLE
        binding.gameLayout.visibility = View.GONE

        binding.roudnCounter.text = roundStr + ronda

        players = arrayOf(
            Player("Jugador 1", binding.playerOneText, binding.playerOneCounter, binding.playerOnePoints),
            Player("Jugador 2", binding.playerTwoText, binding.playerTwoCounter, binding.playerTwoPoints),
            Player("Jugador 3", binding.playerThreeText, binding.playerThreeCounter, binding.playerThreePoints),
            Player("Jugador 4", binding.playerFourText, binding.playerFourCounter, binding.playerFourPoints)
        )

        currentPlayer = players[currentPlayerIndex]

        binding.playerSelector.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.textView11.text = (p1 + 1).toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        binding.holdButton.setOnClickListener { holded() }

        binding.imageView.setOnClickListener {
            val random = Random.nextInt(0, 6) + 1
            animateDiceImage(binding.imageView, random)
            if (random == 1) {
                currentPlayer.currentPoints = 0
            } else {
                currentPlayer.currentPoints += random
            }
            currentPlayer.currentPointsCounter.text = currentPlayer.currentPoints.toString()
        }

    }   // onCreate

    private fun holded(){

        currentPlayerIndex += 1

        if (currentPlayerIndex > players.size - 1) {
            currentPlayerIndex = 0
            ronda += 1

            if (ronda > 5) {
                evaluateWinner()
                return
            }

            binding.roudnCounter.text = String.format("%s %d", roundStr, ronda)
        }

        // Update the total points
        currentPlayer.totalPoints += currentPlayer.currentPoints
        currentPlayer.totalPointsCounter.text = currentPlayer.totalPoints.toString()

        // Reset the current points to 0
        currentPlayer.currentPoints = 0
        currentPlayer.currentPointsCounter.text = "0"

        currentPlayer = players[currentPlayerIndex]
    }

    private fun getWinner() = players.maxBy { it.totalPoints }


    private fun evaluateWinner() {
        val winner = getWinner()
        binding.gameLayout.visibility = View.GONE
        binding.winnerLabel.text = winner.name + " won!!"
        binding.winnerLayout.visibility = View.VISIBLE
    }

    private fun animateDiceImage(imageView: ImageView, randomNumber: Int) {

        val diceImages = arrayOf(R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3, R.drawable.dice_4, R.drawable.dice_5, R.drawable.dice_6)

        imageView.setImageResource(diceImages[randomNumber - 1])
        imageView.maxWidth = 100
        imageView.maxHeight = 100

    }
}