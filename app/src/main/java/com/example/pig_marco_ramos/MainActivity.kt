package com.example.pig_marco_ramos

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pig_marco_ramos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentPlayer: Player
    private var currentPlayerIndex = 0
    private lateinit var players: Array<Player>
    private var ronda = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animDuration: Long = 450

        binding.roudnCounter.text = "Ronda $ronda"

        players = arrayOf(
            Player("Jugador 1", binding.playerOneText, binding.playerOneHold, binding.playerOneCounter),
            Player("Jugador 2", binding.playerTwoText, binding.playerTwoHold, binding.playerTwoCounter),
            Player("Jugador 3", binding.playerThreeText, binding.playerThreeHold, binding.playerThreeCounter),
            Player("Jugador 4", binding.playerFourText, binding.playerFourHold, binding.playerFourCounter)
        )

        currentPlayer = players[currentPlayerIndex]

        for (player in players) {
            player.holdButton.setOnClickListener { holded() }
        }

        binding.imageView.setOnClickListener {
            currentPlayer.currentPoints += 1
            currentPlayer.currentPointsCounter.text = currentPlayer.currentPoints.toString()
        }

    }   // onCreate

    private fun holded(){
        currentPlayerIndex += 1

        if (currentPlayerIndex > players.size - 1) {
            currentPlayerIndex = 0
            ronda += 1
            binding.roudnCounter.text = String.format("Ronda %d", ronda)
        }

        currentPlayer = players[currentPlayerIndex]
    }

    private fun changeUserName() {
    }

    fun my_button_onClick_working(view: View?) {

        // step 1
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val popupView = inflater.inflate(R.layout.popup_window, null)

        // step 2
        val wid = LinearLayout.LayoutParams.WRAP_CONTENT
        val high = LinearLayout.LayoutParams.WRAP_CONTENT
        val focus= true
        val popupWindow = PopupWindow(popupView, wid, high, focus)

        // step 3
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

    }

}