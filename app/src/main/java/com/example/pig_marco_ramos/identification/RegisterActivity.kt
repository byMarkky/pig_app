package com.example.pig_marco_ramos.identification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pig_marco_ramos.api.Service
import com.example.pig_marco_ramos.api.model.ResponseData
import com.example.pig_marco_ramos.databinding.ActivityRegisterBinding
import com.example.pig_marco_ramos.datepicker.DatePickerFragment
import com.example.pig_marco_ramos.room.AppDatabase
import com.example.pig_marco_ramos.room.User
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var user: String
    private lateinit var pass: String
    private lateinit var imageSelected: String
    private lateinit var pictureList: MutableList<String?>
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private var errors: MutableList<String> = ArrayList(4)
    private val MIN_AGE = 16

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pictureList = mutableListOf()

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

                Log.i("IMAGE", "IMAGES SELECTED: $imageSelected")

                val birthDate = binding.registerDate.text.toString()
                val user = User(name = user, password = pass, birthDate = birthDate, image = imageSelected)

                persistUser(user)

                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }

        }

        binding.reloadBtn.setOnClickListener { loadAvatars() }

        binding.imageOne.setOnClickListener { imageSelected = pictureList[0] ?: "" }
        binding.imageTwo.setOnClickListener { imageSelected = pictureList[1] ?: "" }
        binding.imageThree.setOnClickListener { imageSelected = pictureList[2] ?: "" }

        loadAvatars()
    }

    private fun loadAvatars() {
        val gender = if (binding.maleBtn.isChecked) "male" else "female"
        val retrofit = Retrofit.Builder().baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(Service::class.java)
        val call = service.getRandom(gender, 3)

        call.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", "SUCCESSFUL RESPONSE, BODY: ${response.body()}")
                    val results = response.body()?.results

                    pictureList.add(results?.get(0)?.picture?.large)
                    pictureList.add(results?.get(1)?.picture?.large)
                    pictureList.add(results?.get(2)?.picture?.large)

                    Picasso.get().load(results?.get(0)?.picture?.large).into(binding.imageOne)
                    Picasso.get().load(results?.get(1)?.picture?.large).into(binding.imageTwo)
                    Picasso.get().load(results?.get(2)?.picture?.large).into(binding.imageThree)
                } else {
                    Log.e("RESPONSE", "CONNECTION ERROR")
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                Log.e("API", "ERROR ON CALLBACK: $t")
            }
        })
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
            makeSnackBar("You have to accept the terms and conditions")
            return false
        }

        if (userName.length < 4) {
            makeSnackBar("Name must be between 4 and 10 characters")
            return false
        }

        if (!validatePassword(binding.registerPassword.text.toString())) {
            makeSnackBar("Password: 4 - 10 characters and at least 1 digit")
            return false
        }

        if (!validateDate()) {
            makeSnackBar("You have to be 16 y.o. at least")
            return false
        }

        val db = AppDatabase.getInstance(this)
        val userDao = db.getDao()
        if (userDao.exist(userName, userPass)) {
            makeSnackBar("This user already exists")
            return false
        }

        return true
    }

    private fun makeSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show()
    }

    private fun validatePassword(passwd: String): Boolean {

        if (passwd.length < 4) return false

        // Check if password have at least one number
        if (!passwd.contains("[0-9]".toRegex())) return false

        return true
    }

    /**
     * Method that get the current date and parse the selected date
     * to Calendar to evaluate if the user have a valid years
     * @return boolean True if the user age if valid, False if not
     */
    private fun validateDate(): Boolean {
        val currentDate = Calendar.getInstance()
        val selectedDate = Calendar.getInstance()
        val stringDate = binding.registerDate.text.toString().split("/")
        selectedDate.set(stringDate[2].toInt(), stringDate[1].toInt(), stringDate[0].toInt())
        val ageInYears = currentDate.get(Calendar.YEAR) - selectedDate.get(Calendar.YEAR)

        return ageInYears >= MIN_AGE
    }

    private fun showDatePicker() {
        val datePicker = DatePickerFragment { day, mont, year -> onDateSelected(day=day, month = mont, year = year) }
        datePicker.show(supportFragmentManager, "datepicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        this.day = day
        this.month = month
        this.year = year
        binding.registerDate.setText("$day/$month/$year")
    }

}