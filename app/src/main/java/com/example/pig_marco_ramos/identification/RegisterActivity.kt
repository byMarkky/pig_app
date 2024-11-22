package com.example.pig_marco_ramos.identification

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pig_marco_ramos.databinding.ActivityRegisterBinding
import com.example.pig_marco_ramos.datepicker.DatePickerFragment

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var user: String
    private lateinit var pass: String
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = binding.registerName.text.toString()
        pass = binding.registerPassword.text.toString()

        binding.registerDate.setOnClickListener { showDatePicker() }

        binding.regButton.setOnClickListener {

            // Case when the data is invalid
            if (!validate()) return@setOnClickListener

            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun validate(): Boolean {
        // TODO: Validate data and write in the database
        // The user do not accept the terms
        if (!binding.termsCheck.isChecked) {
            Log.d("TERMS", "ACCEPTED")
            return false
        }

        return true
    }

    private fun showDatePicker() {
        val datePicker = DatePickerFragment { day, mont, year -> onDateSelected(day=day, month = mont, year = year) }
        datePicker.show(supportFragmentManager, "datepicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        Log.i("DATE", "YEAR: $year, MONTH: $month, DAY: $day")
        this.day = day
        this.month = month
        this.year = year
        binding.registerDate.setText("$day/$month/$year")
    }

}