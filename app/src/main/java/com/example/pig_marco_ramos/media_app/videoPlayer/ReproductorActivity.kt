package com.example.pig_marco_ramos.media_app.videoPlayer

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.pig_marco_ramos.databinding.ActivityReproductorBinding
import com.example.pig_marco_ramos.room.User
import java.io.File

class ReproductorActivity: AppCompatActivity() {

    private lateinit var binding: ActivityReproductorBinding
    private var PATH: File? = null
    private var user: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityReproductorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = getUserFromExtra()

        PATH = getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + user?.name)

        val videoList = PATH?.listFiles()?.filter { file: File? -> file?.name?.endsWith(".mp4") == true }

        // TODO: Adaptar el aspect ratio del video player para cada tipo de video
        // TODO: Indicar que video se esta reproduciendo con un Toast u otra cosa
        // Inicializar el reproductor si binding no es nulo
        binding.let {
            val player = ExoPlayer.Builder(this).build()
            it.playerView.player = player

            if (videoList != null) {
                for (video in videoList) {
                    val mediaItem = MediaItem.fromUri(video.toUri())
                    player.addMediaItem(mediaItem)
                }
            }

            // TODO: Implements if its possible, auto-return to previous activity
            binding.backButton.setOnClickListener {
                val intent = Intent(this, VideoPlayerActivity::class.java)
                intent.putExtra("USER", user)
                startActivity(intent)
            }

            player.prepare()
            player.play()

        }

    }   // onCreate

    private fun getUserFromExtra(): User? {
        return if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("USER", User::class.java)
        } else {
            intent.getSerializableExtra("USER") as User?
        }
    }


}