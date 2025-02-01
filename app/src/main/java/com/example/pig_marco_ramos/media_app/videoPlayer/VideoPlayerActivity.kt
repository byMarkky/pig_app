package com.example.pig_marco_ramos.media_app.videoPlayer

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pig_marco_ramos.databinding.ActivityVideoplayerBinding
import com.example.pig_marco_ramos.room.User
import java.io.File

class VideoPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoplayerBinding
    private var user: User? = null
    private var PATH: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityVideoplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = getUserFromExtra()

        PATH = getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + user?.name)

        val videoList = PATH?.listFiles()?.filter { file: File? -> file?.name?.endsWith(".mp4") == true }
        val adapter = VideoAdapter(videoList)

        binding.recyclerList.layoutManager = LinearLayoutManager(this)
        binding.recyclerList.adapter = adapter

        binding.playButton.setOnClickListener {
            val intent = Intent(this, ReproductorActivity::class.java)
            intent.putExtra("USER", user)
            startActivity(intent)
        }

    }

    private fun getUserFromExtra(): User? {
        return if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("USER", User::class.java)
        } else {
            intent.getSerializableExtra("USER") as User?
        }
    }

    inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }

    inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelable(key) as? T
    }

}