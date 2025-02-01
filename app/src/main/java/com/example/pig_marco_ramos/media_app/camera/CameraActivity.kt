package com.example.pig_marco_ramos.media_app.camera

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pig_marco_ramos.databinding.ActivityCameraBinding
import com.example.pig_marco_ramos.room.User
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.name

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var imageURI: Uri
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>

    private var IMG_PATH: File? = null
    private var user: User? = null
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")

                binding.pictureButton.setOnClickListener { openCamera(MediaStore.ACTION_IMAGE_CAPTURE) }
                binding.videoButton.setOnClickListener { openCamera(MediaStore.ACTION_VIDEO_CAPTURE) }

            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = getUserFromExtra()

        IMG_PATH = getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + user?.name)

        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("MY CAMERA", "${imageURI.path} | CODE: ${result.resultCode}")
            } else {
                Log.e("CAMERA_VIDE", result.resultCode.toString())
            }
            Log.d("CAMERA RESULT", result.data.toString())
            reloadMediaAdapter()
        }

        onClickRequestPermissions(binding.root)

        binding.videoRecycler.layoutManager = LinearLayoutManager(this)
        binding.videoRecycler.adapter = FilesAdapter(IMG_PATH?.list()) { result -> fileAdapterFunction(result)}

    }   // onCreate

    private fun reloadMediaAdapter() {
        binding.videoRecycler.adapter = FilesAdapter(IMG_PATH?.list()) { result -> fileAdapterFunction(result)}
    }

    private fun fileAdapterFunction(result: String) {
        val path = Path(IMG_PATH.toString() + File.separator + IMG_PATH?.list()?.find { it.contains(result) })
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setMessage(path.name)
        alertDialog.setTitle("Delete this file?")
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { dialog, _ ->
            Log.d("ALERT", "DO NOT DELETE FILE")
            dialog.dismiss()
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { dialog, _ ->
            if (path.deleteIfExists()) {
                Log.d("FILE_delete", "FILE DELETED SUCCESSFULLY")
                reloadMediaAdapter()
            }
            dialog.dismiss()
        }
        alertDialog.show()
        Log.d("RESULT RECYCLER", path.toString())
    }

    private fun getUserFromExtra(): User? {
        return if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("USER", User::class.java)
        } else {
            intent.getSerializableExtra("USER") as User?
        }
    }

    private fun openCamera(type: String) {
        val intent = Intent(type)

        var file: File? = null

        if (type == MediaStore.ACTION_IMAGE_CAPTURE) {
            file = createFile("jpg")
        } else if (type == MediaStore.ACTION_VIDEO_CAPTURE) {
            file = createFile("mp4")
        }

        file?.also {
            imageURI = FileProvider.getUriForFile(
                this,
                //"${BuildConfig.APPLICATION_ID}.fileprovider",
                "com.example.pig_marco_ramos.fileprovider",
                it
            )

            Log.d("IMAGE URI", imageURI.toString())
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI)
            takePictureLauncher.launch(intent)
        }
    }

    private fun createFile(extension: String): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.GERMANY)
        val currentTime = Date()
        val dateString: String = timeStamp.format(currentTime)
        val storageUri = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + user?.name)
        Log.d("DATE_CAMERA", dateString)
        return File.createTempFile(
            "${extension.uppercase(Locale.ROOT)}_${dateString}_",
            ".$extension", storageUri
        )
    }

    private fun onClickRequestPermissions(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                    //startActivity(Intent("android.media.action.IMAGE_CAPTURE"))
                    //openCamera(MediaStore.ACTION_IMAGE_CAPTURE)
                    binding.pictureButton.setOnClickListener { openCamera(MediaStore.ACTION_IMAGE_CAPTURE) }
                    binding.videoButton.setOnClickListener { openCamera(MediaStore.ACTION_VIDEO_CAPTURE) }
                    Log.d("CAMERA", "HAVE PERMISSIONS TO USE CAMERA")
                }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.CAMERA
            ) -> {
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            } else -> {
                requestPermissionLauncher.launch(
                    android.Manifest.permission.CAMERA
                )
            }
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