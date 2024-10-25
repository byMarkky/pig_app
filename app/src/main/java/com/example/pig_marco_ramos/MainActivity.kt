package com.example.pig_marco_ramos

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pig_marco_ramos.databinding.ActivityMainBinding
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentPlayer: Player
    private lateinit var defaultPlayers: Array<Player>
    private var players : MutableList<Player> = mutableListOf()
    private var currentPlayerIndex = 0
    private var ronda = 1
    private val roundStr = "Round "
    private var nPlayers = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Just the default players
        defaultPlayers = arrayOf(
            Player("Player 1", binding.playerOneText, binding.playerOneCounter, binding.playerOnePoints, true),
            Player("Player 2", binding.playerTwoText, binding.playerTwoCounter, binding.playerTwoPoints, true),
            Player("Player 3", binding.playerThreeText, binding.playerThreeCounter, binding.playerThreePoints, true),
            Player("Player 4", binding.playerFourText, binding.playerFourCounter, binding.playerFourPoints, true)
        )

        binding.playerSelector.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                nPlayers = p1 + 2
                binding.textView11.text = nPlayers.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        binding.playButton.setOnClickListener {
            for (i in 0..<nPlayers) {
                defaultPlayers[i].disable = false
                players.add(i, defaultPlayers[i])
            }

            binding.startLayout.visibility = View.GONE
            binding.gameLayout.visibility = View.VISIBLE

            startGame()
        }

    }   // onCreate

    private fun startGame() {

        binding.startLayout.visibility = View.GONE
        binding.gameLayout.visibility = View.VISIBLE

        binding.roudnCounter.text = roundStr + ronda

        currentPlayer = players[currentPlayerIndex]

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
    }

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
    }   // holded

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