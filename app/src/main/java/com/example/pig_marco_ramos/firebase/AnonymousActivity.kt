package com.example.pig_marco_ramos.firebase

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pig_marco_ramos.databinding.ActivityAnonymousBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class AnonymousActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnonymousBinding

    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAnonymousBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.search.setOnClickListener {
            //if (!binding.editTextText.text.isNullOrEmpty()) return@setOnClickListener
            Log.d("SEARCH", "GONNA SEARCH ${binding.editTextText.text.toString()}")
            if (binding.nifFlag.isChecked) {
                Log.d("SEARCH", "SEARCH BY NIF")
                getByNif(binding.editTextText.text.toString())
            } else {
                Log.d("SEARCH", "SEARCH BY NIA")
                getByNia(binding.editTextText.text.toString())
            }
        }

        auth.signInAnonymously().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInAnonymously:success")
                val user = auth.currentUser
                Log.d(TAG, "USERNAME UID: ${ user?.uid }")
                getDataAlumnos()
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInAnonymously:failure", task.exception)
                Toast.makeText(
                    baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
                //updateUI(null)
            }
        }
    }   // onCreate

    private fun getByNif(nif: String) {
        val docRef = db.collection("alumnosMRM")
        docRef.whereEqualTo("nif", nif).get()
            .addOnSuccessListener { result ->
                if (result != null && result.documents.isNotEmpty()) {
                    val data = result.documents[0].data
                    val headers = mutableListOf("NIF", "NIA", "Nombre", "Apellido", "Media")
                    Log.d("GET_NIA", "DATA COLLECTED: ${ result.documents[0].data?.values.toString() }")
                    headers.add(data?.get("nif").toString())
                    headers.add(data?.get("nia").toString())
                    headers.add(data?.get("nombre").toString())
                    headers.add(data?.get("apellido").toString())
                    headers.add(data?.get("mediaexpediente").toString())

                    binding.gridSingle.adapter = GridAdapter(headers)
                }
            }
    }

    private fun getByNia(nia: String) {
        val docRef = db.collection("alumnosMRM")
        docRef.whereEqualTo("nia", nia).get()
            .addOnSuccessListener { result ->
                if (result != null && result.documents.isNotEmpty()) {
                    val data = result.documents[0].data
                    val headers = mutableListOf("NIF", "NIA", "Nombre", "Apellido", "Media")
                    Log.d("GET_NIA", "DATA COLLECTED: ${ result.documents[0].data?.values.toString() }")
                    headers.add(data?.get("nif").toString())
                    headers.add(data?.get("nia").toString())
                    headers.add(data?.get("nombre").toString())
                    headers.add(data?.get("apellido").toString())
                    headers.add(data?.get("mediaexpediente").toString())

                    binding.gridSingle.adapter = GridAdapter(headers)
                }
            }
    }

    private fun getDataAlumnos() {
        val docRef = db.collection("alumnosMRM")
        docRef.get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    val dataSet = mutableListOf("")
                    dataSet.clear()
                    //Log.d("DocumentSnapshot data: ${ result.documents[0].data?.values }")
                    for (doc in result.documents) {
                        val data = doc.data
                        Log.d(TAG, "DocumentSnapshot data: ${doc.data?.get("nif")}")
                        dataSet.add(data?.get("nombre").toString())
                        dataSet.add(data?.get("apellido").toString())
                        dataSet.add(data?.get("mediaexpediente").toString())
                        binding.gridAll.adapter = GridAdapter(dataSet)
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }
}