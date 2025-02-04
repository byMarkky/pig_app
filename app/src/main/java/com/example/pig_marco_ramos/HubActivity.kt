package com.example.pig_marco_ramos

import android.content.Intent
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.pig_marco_ramos.databinding.ActivityHubBinding
import com.example.pig_marco_ramos.firebase.FirebaseActivity
import com.example.pig_marco_ramos.games.PIG.MainActivity
import com.example.pig_marco_ramos.games.chuck.ChuckJokes
import com.example.pig_marco_ramos.media_app.camera.CameraActivity
import com.example.pig_marco_ramos.media_app.videoPlayer.VideoPlayerActivity
import com.example.pig_marco_ramos.room.User

class HubActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHubBinding
    private lateinit var sound: SoundPool

    private var soundPaused = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sound()

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

        binding.cameraLauncher.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("USER", user)
            startActivity(intent)
        }

        binding.playerLauncher.setOnClickListener {
            val intent = Intent(this, VideoPlayerActivity::class.java)
            intent.putExtra("USER", user)
            startActivity(intent)
        }

        binding.audioButton.setOnClickListener { soundButtonMgr() }

    }

    private fun soundButtonMgr() {
        if (soundPaused) {
            sound.autoResume()
            binding.audioButton.setImageResource(android.R.drawable.ic_media_pause)
            Log.d("AUDIO", "AUDIO PLAYING")
        } else {
            sound.autoPause()
            binding.audioButton.setImageResource(android.R.drawable.ic_media_play)
            Log.d("AUDIO", "AUDIO PAUSED")
        }

        soundPaused = !soundPaused
    }

    private fun sound() {
        sound = SoundPool.Builder().build()
        val soundID = sound.load(this, R.raw.sample_audio, 1)
        sound.setOnLoadCompleteListener { pool, id, status ->
            if (status == 0) {
                val streamID = pool.play(soundID,0.5F,0.5F,1,-1,1F)
                Log.d("AUDIO", "Audio Loaded Successful")
            } else Log.d("AUDIO", "STATUS: $status")

            binding.audioButton.setImageResource(android.R.drawable.ic_media_pause)
            soundPaused = false
        }

    }

}