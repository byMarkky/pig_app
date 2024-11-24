package com.example.pig_marco_ramos.identification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pig_marco_ramos.databinding.ActivityIdentificationBinding
import com.example.pig_marco_ramos.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        binding.button.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val db = AppDatabase.getInstance(this@IdentifyActivity)
                val userDao = db.getDao()
                userDao.clean()
                val users = userDao.getAll()
                if (users.isEmpty()) {
                    Log.d("USER", "DATABASE EMPTY")
                } else {
                    for (user in userDao.getAll()) {
                        Log.d("USERS", "$user")
                    }
                }
            }
        }

    }

}