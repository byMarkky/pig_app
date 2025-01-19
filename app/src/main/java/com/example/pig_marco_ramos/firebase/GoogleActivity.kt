package com.example.pig_marco_ramos.firebase

import android.app.DownloadManager
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.pig_marco_ramos.R
import com.example.pig_marco_ramos.databinding.ActivityGoogleBinding
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.atomic.AtomicBoolean


class GoogleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoogleBinding
    private lateinit var signInClient: SignInClient
    private lateinit var auth: FirebaseAuth
    private val TAG = "GOOGLE AUTH"
    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            result ->
        handleSignInResult(result.data)
    }

    private val db = Firebase.firestore
    private var isSigned = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoogleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signInClient = Identity.getSignInClient(this@GoogleActivity)

        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.d(TAG, "CURRENT USER IS NULL")
        } else {
            Log.d(TAG, "CURRENT USER $currentUser")
        }

        signIn()

    }


    private fun userExists(nif: String, nia: String, callback: (Boolean) -> Unit) {
        val usersCollection = db.collection(getString(R.string.firebaseDocument)) // Replace "users" with your collection name

        val query: Query = usersCollection
            .whereEqualTo("nif", nif) // Replace "NIF" with your actual field name
            //.whereEqualTo("nia", nia) // Replace "NIA" with your actual field name

        query.get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) callback(false)  // User not exits
                else if (querySnapshot.documents[0].data?.get("nia") == nia) callback(true)
                else callback(true)
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                callback(false) // Handle error and return false
            }
    }

    private fun postData() {
        // Check if the input texts are empty or null
        if (binding.nifInput.text.isNullOrEmpty() || binding.niaInput.text.isNullOrEmpty() ||
            binding.nameInput.text.isNullOrEmpty() || binding.lastInput.text.isNullOrEmpty() ||
            binding.avgInput.text.isNullOrEmpty()) {
            Snackbar.make(binding.root, "Datos incompletos", Snackbar.LENGTH_LONG).show()
            return;
        }

        val docData = hashMapOf(
            "nif" to binding.nifInput.text.toString(),
            "nia" to binding.niaInput.text.toString(),
            "nombre" to binding.nameInput.text.toString(),
            "apellido" to binding.lastInput.text.toString(),
            "mediaexpediente" to binding.avgInput.text.toString().toDouble()
        )
        Log.d(TAG, "$docData")

        userExists(binding.nifInput.text.toString(),binding.niaInput.text.toString()) { res ->
            if (res) {
                Snackbar.make(binding.root, "YA EXISTE ESE USUARIO", Snackbar.LENGTH_LONG).show()
                return@userExists
            }

            db.collection(getString(R.string.firebaseDocument))
                .add(docData)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                    Snackbar.make(binding.root, "Datos subidos correctamente", Snackbar.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    Snackbar.make(binding.root, "Error subiendo los datos", Snackbar.LENGTH_LONG).show()
                }

        }

    }

    private fun handleSignInResult(data: Intent?) {
        try {
            val credential = signInClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                Log.d("TAG", "firebaseAuthWihGoogle: ${credential.id}")
                firebaseAuthWithGoogle(idToken)
            }
        } catch (e: ApiException) {
            Log.w(TAG, "Google sign in failed", e)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this@GoogleActivity) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "SignInWithCredential:success")
                    val user = auth.currentUser
                    Log.d(TAG, "USER: $user")

                    // Prevent the user send data without authentication and get an error
                    binding.saveBtn.setOnClickListener { postData() }

                } else {
                    Log.w(TAG, "SignInWithCredential:failure", task.exception)
                    Snackbar.make(binding.root, "Auth failed", Snackbar.LENGTH_LONG).show()
                }
            }
    }

    private fun signIn() {
        val signInRequest = GetSignInIntentRequest.builder()
            .setServerClientId(getString(R.string.google_id))
            .build()

        signInClient.getSignInIntent(signInRequest)
            .addOnSuccessListener { pendingIntent ->
                launchSignIn(pendingIntent)

                isSigned = true
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Google Sign-In failed", e)
            }
            .addOnCompleteListener {
                if (isSigned) {
                    Log.d(TAG, "USER SIGNED")
                } else {
                    Log.d(TAG, "USER NOT SIGNED")
                }
            }
    }

    private fun launchSignIn(pendingIntent: PendingIntent) {
        try {
            val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent)
                .build()
            signInLauncher.launch(intentSenderRequest)
        } catch (e: IntentSender.SendIntentException) {
            Log.e(TAG, "Couldn't start Sign In:  ${e.localizedMessage}")
        }
    }


}