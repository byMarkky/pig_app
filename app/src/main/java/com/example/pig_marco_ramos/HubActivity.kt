package com.example.pig_marco_ramos

import android.animation.Animator
import android.content.Intent
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pig_marco_ramos.audio.AudioActivity
import com.example.pig_marco_ramos.databinding.ActivityHubBinding
import com.example.pig_marco_ramos.firebase.FirebaseActivity
import com.example.pig_marco_ramos.games.PIG.MainActivity
import com.example.pig_marco_ramos.games.chuck.ChuckJokes
import com.example.pig_marco_ramos.media_app.camera.CameraActivity
import com.example.pig_marco_ramos.media_app.videoPlayer.VideoPlayerActivity
import com.example.pig_marco_ramos.room.User
import kotlin.math.log

class HubActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHubBinding
    private lateinit var sound: SoundPool

    private var soundPaused = true
    private var streamId = 0
    private var soundId = 0

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

        loggedAnimation()

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

        val lottie = binding.audioButton
        lottie.repeatCount = 0
        lottie.playAnimation()

        binding.audioButton.setOnClickListener {
            soundButtonMgr()

            lottie.setMaxFrame(60)
            lottie.setMinFrame(0)
            if (lottie.isAnimating) {
                // If the animation is currently playing, stop and reset it
                lottie.cancelAnimation() // Stop the animation
                lottie.progress = 0f // Reset to the beginning
            } else {
                // If the animation is not playing, play it
                lottie.reverseAnimationSpeed()
                lottie.playAnimation()
            }

        }

        binding.audioLauncher.setOnClickListener {
            val intent = Intent(this, AudioActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loggedAnimation() {
        val logged = binding.loggedAnimation
        logged.repeatCount = 0
        logged.speed = 1.5f
        logged.playAnimation()

        logged.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                binding.gridLayout.visibility = View.VISIBLE
                logged.visibility = View.GONE

                val animation = AnimationSet(true)
                logged.animation = animation

                val fadeOut: Animation = AlphaAnimation(1f, 0f)
                fadeOut.startOffset = logged.duration
                fadeOut.duration = 500

                animation.addAnimation(fadeOut)
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }

        })

    }

    override fun onStart() {
        super.onStart()
        sound()

    }

    private fun soundButtonMgr() {
        if (soundPaused) {
            sound.autoResume()
            Log.d("AUDIO", "AUDIO PLAYING")
        } else {
            sound.autoPause()
            Log.d("AUDIO", "AUDIO PAUSED")
        }

        soundPaused = !soundPaused
    }

    private fun sound() {
        sound = SoundPool.Builder().build()
        soundId = sound.load(this, R.raw.background_music, 1)
        sound.setOnLoadCompleteListener { pool, _, status ->
            if (status == 0) {
                this@HubActivity.streamId = pool.play(soundId,0.5F,0.5F,1,-1,1F)
                Log.d("AUDIO", "Audio Loaded Successful")
            } else Log.d("AUDIO", "STATUS: $status")

            soundPaused = false
        }

    }

    override fun onResume() {
        super.onResume()
        sound.autoResume()
    }

    override fun onPause() {
        super.onPause()
        sound.stop(streamId)
        soundPaused = true
    }

    override fun onDestroy() {
        super.onDestroy()
        sound.stop(streamId)
        sound.unload(soundId)
        sound.release()
        Log.d("AUDIO", "SoundPool RELEASED")
    }

}