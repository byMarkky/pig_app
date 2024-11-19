package com.example.pig_marco_ramos.identification

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pig_marco_ramos.databinding.ActivityIdentificationBinding

class IdentifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIdentificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityIdentificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            val intent = Intent(this@IdentifyActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.registerBtn.setOnClickListener {
            val intent = Intent(this@IdentifyActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

}