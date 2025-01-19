package com.example.pig_marco_ramos

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.pig_marco_ramos.games.PIG.MainActivity
import com.example.pig_marco_ramos.databinding.ActivityHubBinding
import com.example.pig_marco_ramos.firebase.FirebaseActivity
import com.example.pig_marco_ramos.games.chuck.ChuckJokes
import com.example.pig_marco_ramos.room.User

class HubActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("USER", User::class.java)
        } else {
            intent.getSerializableExtra("USER")
        }

        binding = ActivityHubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pigLaunchBtn.setOnClickListener {
            val intent = Intent(this@HubActivity, MainActivity::class.java)
            startActivity(intent)
        }

        binding.chuckLaunchBtn.setOnClickListener {
            val intent = Intent(this@HubActivity, ChuckJokes::class.java)
            startActivity(intent)
        }

        binding.firebaseLaunchBtn.setOnClickListener {
            val intent = Intent(this@HubActivity, FirebaseActivity::class.java)
            intent.putExtra("USER", user)
            startActivity(intent)
        }

    }

}