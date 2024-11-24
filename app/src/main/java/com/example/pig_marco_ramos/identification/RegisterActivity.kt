package com.example.pig_marco_ramos.identification

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.pig_marco_ramos.databinding.ActivityRegisterBinding
import com.example.pig_marco_ramos.datepicker.DatePickerFragment
import com.example.pig_marco_ramos.room.AppDatabase
import com.example.pig_marco_ramos.room.User
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Period
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var user: String
    private lateinit var pass: String
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private var errors: MutableList<String> = ArrayList(4)
    private val _userExists = MutableLiveData<Boolean>()
    private val userExists: LiveData<Boolean> get() = _userExists

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerDate.setOnClickListener { showDatePicker() }

        binding.regButton.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {
                // Case when the data is invalid
                if (!validate()) {
                    if (errors.isNotEmpty()) {
                        Snackbar.make(binding.root, errors[0], Snackbar.LENGTH_LONG).show()
                    }
                    return@launch
                }

                user = binding.registerName.text.toString()
                pass = binding.registerPassword.text.toString()

                Log.d("NAME", "NAME: $user")
                Log.d("NAME", "PASS: $pass")
                val birthDate = binding.registerDate.text.toString()
                val user = User(name = user, password = pass, birthDate = birthDate)

                persistUser(user)

                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }

        }

    }

    private fun persistUser(user: User) {
        val db = AppDatabase.getInstance(this)
        val userDao = db.getDao()
        userDao.insert(user)
    }

    private fun validate(): Boolean {
        val userName = binding.registerName.text.toString()
        val userPass = binding.registerPassword.text.toString()

        // The user do not accept the terms
        if (!binding.termsCheck.isChecked) {
            errors.add("Tienes que aceptar los terminos y condiciones")
            return false
        }

        if (userName.length < 4) {
            errors.add("La contraseña debe tener un minimo de 4 y maximo 10 caracteres")
            return false
        }

        if (!validatePassword(binding.registerPassword.text.toString())) {
            errors.add("La contraseña tener un minimo de 4 y maximo 10 caracteres")
            return false
        }

        if (!validateDate(16)) {
            errors.add("Debes tener mas de 16 años para registrarte")
            return false
        }

        val db = AppDatabase.getInstance(this)
        val userDao = db.getDao()
        if (userDao.exist(userName, userPass)) {
            errors.add("El usuario ya existe, pruebe otro ó logeate")
            return false
        }

        return true
    }

    private fun validatePassword(passwd: String): Boolean {

        if (passwd.length < 4) return false

        // Check if password have at least one number
        if (passwd.contains(".*\\d.*")) {
            Log.i("PASSWORD", "THE PASSWORD MUST HAVE AT LEAST ONE NUMBER")
            return false
        }

        return true
    }

    /**
     * Method that get the current date and parse the selected date
     * to Calendar to evaluate if the user have a valid years
     * @param minAge Minimum age to be compared
     * @return boolean True if the user age if valid, False if not
     */
    private fun validateDate(minAge: Int): Boolean {
        val currentDate = Calendar.getInstance()
        val selectedDate = Calendar.getInstance()
        val stringDate = binding.registerDate.text.toString().split("/")
        selectedDate.set(stringDate[2].toInt(), stringDate[1].toInt(), stringDate[0].toInt())
        val ageInYears = currentDate.get(Calendar.YEAR) - selectedDate.get(Calendar.YEAR)

        return ageInYears >= minAge
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