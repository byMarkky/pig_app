package com.example.pig_marco_ramos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pig_marco_ramos.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinnerData = arrayOf("Select the number of players", 2, 3, 4)

        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerData)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinner = binding.spinner
        spinner.setSelection(0)
        spinner.adapter = aa


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val intent = Intent(this@MainActivity, PlayerActivity::class.java)
                val roundText = binding.editTextNumber.text

                if (p2 != 0 && roundText.toString() != "") {
                    intent.putExtra("PLAYER_NUMBER", spinnerData[p2])
                    intent.putExtra("ROUND_NUMBER", roundText.toString())
                    startActivity(intent)
                }

                //showToast(message = spinnerData[p2].toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                showToast(message = "Nothing selected")
            }

        }

    }   // onCreate

    private fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }

}