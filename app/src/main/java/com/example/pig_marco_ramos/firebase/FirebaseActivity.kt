package com.example.pig_marco_ramos.firebase

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.pig_marco_ramos.databinding.ActivityFirebaseBinding
import com.example.pig_marco_ramos.room.User
import com.squareup.picasso.Picasso
import java.io.Serializable

class FirebaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirebaseBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFirebaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.noAuthBtn.setOnClickListener {
            val intent = Intent(this@FirebaseActivity, AnonymousActivity::class.java)
            startActivity(intent)
        }

        binding.googleBtn.setOnClickListener {
            val intent = Intent(this@FirebaseActivity, GoogleActivity::class.java)
            startActivity(intent)
        }

        val user: User? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("USER", User::class.java)
        } else {
            Log.d("SERIALIZABLE", "CAN NOT DE-SERIALIZABLE USER")
            null
        }

        Picasso.get().load(user?.image?.toUri()).into(binding.imageView)

        binding.userText.text = "Usuario: ${user?.name}"

    }
}