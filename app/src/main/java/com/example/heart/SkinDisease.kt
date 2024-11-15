package com.example.heart

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.heart.BACKENED.skin.RetrofitInstance
import com.example.heart.databinding.ActivitySkinDiseaseBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class SkinDisease : AppCompatActivity() {
    private lateinit var binding: ActivitySkinDiseaseBinding
    private var selectedImageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySkinDiseaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermissions()

        binding.cameraBtn.setOnClickListener { openCamera() }
        binding.galleryBtn.setOnClickListener { openGallery() }
        binding.predictBtn.setOnClickListener { predictSkinDisease() }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                CAMERA_REQUEST_CODE
            )
        }
    }

    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE = 2

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    if (bitmap != null) {
                        binding.skinImage.setImageBitmap(bitmap)
                        selectedImageFile = bitmapToFile(bitmap, "camera_image.jpg")
                    } else {
                        Toast.makeText(this, "Failed to capture image.", Toast.LENGTH_SHORT).show()
                    }
                }
                GALLERY_REQUEST_CODE -> {
                    val imageUri = data?.data
                    imageUri?.let {
                        binding.skinImage.setImageURI(it)
                        selectedImageFile = File(getRealPathFromURI(it) ?: return)
                    }
                }
            }
        }
    }

    private fun bitmapToFile(bitmap: Bitmap, fileName: String): File? {
        return try {
            val file = File(cacheDir, fileName)
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                return it.getString(columnIndex)
            }
        }
        return null
    }

    private fun predictSkinDisease() {
        if (selectedImageFile == null) {
            Toast.makeText(this, "Please select an image first!", Toast.LENGTH_SHORT).show()
            return
        }

        val requestFile = selectedImageFile!!.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", selectedImageFile!!.name, requestFile)

        binding.progressBar.visibility = View.VISIBLE
        binding.predictBtn.isEnabled = false

        Handler(Looper.getMainLooper()).postDelayed({
            val api = RetrofitInstance.api

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = api.detectSkinDisease(body)
                    withContext(Dispatchers.Main) {
                        binding.progressBar.visibility = View.GONE
                        binding.predictBtn.isEnabled = true

                        if (response.isSuccessful && response.body() != null) {
                            val result = response.body()?.result ?: "No result found"
                            binding.resTxt.text = "Result: "
                            binding.predTxt.text = result
                        } else {
                            Toast.makeText(this@SkinDisease, "Prediction failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        binding.progressBar.visibility = View.GONE
                        binding.predictBtn.isEnabled = true
                        Toast.makeText(this@SkinDisease, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }, 2000)
    }
}
