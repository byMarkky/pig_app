package com.example.pig_marco_ramos.PIG

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.os.BundleCompat
import com.example.pig_marco_ramos.HubActivity
import com.example.pig_marco_ramos.databinding.ActivityWinnerBinding
import com.example.pig_marco_ramos.player.PlayerDClass

class WinnerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWinnerBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWinnerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.exitButton.setOnClickListener { startActivity(Intent(this@WinnerActivity, HubActivity::class.java)) }

        val nPlayers = intent.getIntExtra("PLAYER_NUMBER", 2)
        val players: MutableList<PlayerDClass?> = mutableListOf()
        val labels: MutableList<TextView> = mutableListOf(binding.winner, binding.second, binding.third, binding.four)

        for (i in 0..<nPlayers) {

            // If android API is less than 33 use the old getParcelable method
            // I use this because in my test device I have the android API 25
            val player: PlayerDClass? = if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) intent.getParcelableExtra("PLAYER_" + (i + 1), PlayerDClass::class.java)
            else intent.getParcelableExtra("PLAYER_" + (i + 1))
            players.add(player)
        }

        val sorted = players.sortedByDescending { it?.totalPoints }

        for (i in 0..<nPlayers) {
            println(sorted[i]?.name + " " + sorted[i]?.totalPoints)
            labels[i].text = "${i + 1}. ${ sorted[i]?.name } - Points: ${ sorted[i]?.totalPoints}"
        }


    }

//    inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
//        SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
//        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as  T?
//    }

//    inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
//        SDK_INT >= 33 -> getParcelable(key, T::class.java)
//        else -> @Suppress("DEPRECATION") getParcelable(key) as  T?
//    }

}