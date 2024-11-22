package com.example.pig_marco_ramos

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pig_marco_ramos.databinding.ActivityGameBinding
import com.example.pig_marco_ramos.player.Player
import com.example.pig_marco_ramos.player.PlayerDClass
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private lateinit var currentPlayer: Player
    private lateinit var defaultPlayers: Array<Player>
    private var players : MutableList<Player> = mutableListOf()
    private var ranking: MutableList<Player> = mutableListOf()
    private var currentPlayerIndex = 0
    private var ronda = 1
    private val roundStr = "Round "
    private var nRounds = 1
    private var nPlayers = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Just the default players
        defaultPlayers = arrayOf(
            Player("Player 1", binding.playerOneText, binding.playerOneCounter, binding.playerOnePoints, true),
            Player("Player 2", binding.playerTwoText, binding.playerTwoCounter, binding.playerTwoPoints, true),
            Player("Player 3", binding.playerThreeText, binding.playerThreeCounter, binding.playerThreePoints, true),
            Player("Player 4", binding.playerFourText, binding.playerFourCounter, binding.playerFourPoints, true)
        )

        nPlayers = intent.getIntExtra("PLAYER_NUMBER", 2)
        nRounds = intent.getStringExtra("ROUND_NUMBER")?.toIntOrNull()!!    // Parse from String? to Int
        println("nPlayers: $nPlayers")
        println("nRounds: $nRounds")

        configPlayers()
        startGame()

    }   // onCreate

    private fun shufflePlayers() {
        players.shuffle()
    }

    private fun configPlayers() {
        shufflePlayers()
        // Add and activate the players of the game
        for (i in 0..<nPlayers) {
            val name = intent.getStringExtra("PLAYER_$i")
            defaultPlayers[i].disable = false
            defaultPlayers[i].label?.text = name ?: ""
            defaultPlayers[i].name = name
            println(intent.getStringExtra("PLAYER_$i"))
            players.add(i, defaultPlayers[i])
        }
    }

    /**
     * Method to start the game logic
     */
    private fun startGame() {

        binding.roudnCounter.text = roundStr + ronda

        currentPlayer = players[currentPlayerIndex]

        binding.holdButton.setOnClickListener { holded() }

        /**
         * When image of the dice is press, generate a random number
         * between 1 and 6, and put the corresponding dice image
         * in the dice imageView.
         * Also add the number to the current player current points
         */
        binding.dice.setOnClickListener {
            val random = Random.nextInt(0, 6) + 1
            animateDiceImage(binding.dice, random)
            if (random == 1) {
                currentPlayer.currentPoints = 0
            } else {
                currentPlayer.currentPoints += random
            }
            currentPlayer.currentPointsCounter?.text = currentPlayer.currentPoints.toString()
        }
    }

    /**
     * Method to change which user will play the turn
     */
    private fun holded(){

        currentPlayerIndex += 1

        currentPlayer.totalPoints += currentPlayer.currentPoints
        if (currentPlayerIndex > players.size - 1) {
            currentPlayerIndex = 0
            ronda += 1

            if (ronda > nRounds) {
                displayScores()
                return
            }

            binding.roudnCounter.text = String.format("%s %d", roundStr, ronda)
        }

        // Update the total points
        //currentPlayer.totalPoints += currentPlayer.currentPoints
        currentPlayer.totalPointsCounter?.text = currentPlayer.totalPoints.toString()

        // Reset the current points to 0
        currentPlayer.currentPoints = 0
        currentPlayer.currentPointsCounter?.text = "0"

        currentPlayer = players[currentPlayerIndex]
    }   // holded

    private fun displayScores() {
        val intent = Intent(this@GameActivity, WinnerActivity::class.java)
        intent.putExtra("PLAYER_NUMBER", players.size)
        for (i in 0..<players.size) {
            val playerName = "PLAYER_" + (i + 1)

            intent.putExtra(playerName, PlayerDClass(players[i].name, players[i].totalPoints))
        }
        startActivity(intent)
    }

    /**
     * Method to display the dice face
     */
    private fun animateDiceImage(imageView: ImageView, randomNumber: Int) {

        val diceImages = arrayOf(R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3, R.drawable.dice_4, R.drawable.dice_5, R.drawable.dice_6)

        imageView.setImageResource(diceImages[randomNumber - 1])
        imageView.maxWidth = 100
        imageView.maxHeight = 100

    }
}