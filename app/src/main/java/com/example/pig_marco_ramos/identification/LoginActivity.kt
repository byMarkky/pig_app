package com.example.pig_marco_ramos.identification

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.pig_marco_ramos.HubActivity
import com.example.pig_marco_ramos.databinding.ActivityLoginBinding
import com.example.pig_marco_ramos.datastore.DataStoreManager
import com.example.pig_marco_ramos.room.AppDatabase
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreManager = DataStoreManager(this)

        lifecycleScope.launch {
            dataStoreManager.userData.collect { (name, passwd) ->
                if (!name.isNullOrEmpty() && !passwd.isNullOrEmpty()) {
                    binding.loginName.setText(name)
                    binding.loginPassword.setText(passwd)
                } else {
                    binding.loginName.setText("")
                    binding.loginPassword.setText("")
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()

        binding.loginName.doAfterTextChanged { deselectCheckBox() }
        binding.loginPassword.doAfterTextChanged { deselectCheckBox() }

        binding.logButton.setOnClickListener {

            val user = binding.loginName.text.toString()
            val passwd = binding.loginPassword.text.toString()
            val remember = binding.rememberCheck

            if (binding.loginName.text.toString().isEmpty()
                || binding.loginPassword.text.toString().isEmpty()) return@setOnClickListener

            if (remember.isChecked) {
                lifecycleScope.launch { dataStoreManager.saveUser(user, passwd) }
            }

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val db = AppDatabase.getInstance(applicationContext)
                    val userDao = db.getDao()
                    val name = binding.loginName.text.toString().trim()
                    val pass = binding.loginPassword.text.toString().trim()

                    if (userDao.exist(name, pass)) {

                        // Do here so the user can't see the changes
                        if (!remember.isChecked) lifecycleScope.launch { dataStoreManager.clearPreferences() }

                        val intent = Intent(this@LoginActivity, HubActivity::class.java)
                        startActivity(intent)
                    } else {
                        Snackbar.make(
                            binding.root,
                            "User $user do not exists, please register.",
                            Snackbar.LENGTH_LONG).setAction("Register") {
                                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                                startActivity(intent)
                        }.show()
                    }

                }
            }
        }
    }

    private fun deselectCheckBox() {
        binding.rememberCheck.isChecked = false
    }

}