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
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        var ronda = 1
        setContentView(binding.root)

        val animDuration: Long = 450

        binding.roudnCounter.text = "Ronda $ronda"

        val players = arrayOf(
            Player("Jugador 1", binding.playerOneText, binding.playerOneHold),
            Player("Jugador 2", binding.playerTwoText,binding.playerTwoHold),
            Player("Jugador 3", binding.playerThreeText,binding.playerThreeHold),
            Player("Jugador 4", binding.playerFourText,binding.playerFourHold)
        )

        for (player in players) {
            player.holdButton.setOnClickListener {
                holded()
            }
        }
    }   // onCreate

    private fun holded(){
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