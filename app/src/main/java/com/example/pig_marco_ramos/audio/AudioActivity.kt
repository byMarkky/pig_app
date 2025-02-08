package com.example.pig_marco_ramos.audio

import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pig_marco_ramos.R
import com.example.pig_marco_ramos.databinding.ActivityAudioBinding
import com.google.android.material.snackbar.Snackbar

class AudioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioBinding
    private lateinit var sound: SoundPool

    private var soundOneId = 0
    private var soundTwoId = 0
    private var soundThreeId = 0
    private var streamOneId = 0
    private var streamTwoId = 0
    private var streamThreeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sound = SoundPool.Builder().build()

        soundOneId = sound.load(this, R.raw.fein, 1)
        soundTwoId = sound.load(this, R.raw.mario, 1)
        soundThreeId = sound.load(this, R.raw.patrick, 1)

    }

    override fun onStart() {
        super.onStart()
        binding.audioOneBtn.setOnClickListener {
            streamOneId = if (binding.toggleOne.isChecked)
                    sound.play(soundOneId,1F,1F,1, -1 ,1F)
                else
                    sound.play(soundOneId,1F,1F,1, 0 ,1F)

        }
        binding.audioTwoBtn.setOnClickListener {
            streamTwoId = if (binding.toggleTwo.isChecked)
                    sound.play(soundTwoId,1F,1F,1, -1 ,1F)
                else
                    sound.play(soundTwoId,1F,1F,1, 0 ,1F)

        }
        binding.audioThreeBtn.setOnClickListener {
            streamThreeId = if (binding.toggleTwo.isChecked)
                sound.play(soundThreeId,1F,1F,1, -1 ,1F)
            else
                sound.play(soundTwoId,1F,1F,1, 0 ,1F)

        }

        setVolumeListeners()

        setPitchListeners()

        setToggleListeners()


    }   // onStart

    private fun setToggleListeners() {
        binding.toggleOne.setOnClickListener {
            Snackbar.make(binding.root, "Play the audio again to see loop changes.", Snackbar.LENGTH_LONG).show()
        }
        binding.toggleTwo.setOnClickListener {
            Snackbar.make(binding.root, "Play the audio again to see loop changes.", Snackbar.LENGTH_LONG).show()
        }
        binding.toggleThree.setOnClickListener {
            Snackbar.make(binding.root, "Play the audio again to see loop changes.", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun setPitchListeners() {
        binding.pitchOne.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p1 >= 0.5) sound.setRate(streamOneId, p1.toFloat() / 50)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        binding.pitchTwo.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p1 >= 0.5) sound.setRate(streamTwoId, p1.toFloat() / 50)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        binding.pitchThree.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p1 >= 0.5) sound.setRate(streamThreeId, p1.toFloat() / 50)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    private fun setVolumeListeners() {
        binding.volumeOne.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                sound.setVolume(streamOneId, p1.toFloat() / 100, p1.toFloat() / 100)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
        binding.volumeTwo.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                sound.setVolume(streamTwoId, p1.toFloat() / 100, p1.toFloat() / 100)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
        binding.volumeThree.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                sound.setVolume(streamThreeId, p1.toFloat() / 100, p1.toFloat() / 100)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
    }


}