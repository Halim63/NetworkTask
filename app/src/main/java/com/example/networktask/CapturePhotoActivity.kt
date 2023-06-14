package com.example.networktask

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.networktask.databinding.ActivityCapturePhotoBinding
import com.example.networktask.viewmodel.CapturePhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_capture_photo.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Calendar
import java.util.Date


@AndroidEntryPoint
class CapturePhotoActivity : AppCompatActivity() {

    val capturePhotoViewModel by viewModels<CapturePhotoViewModel>()

    private lateinit var binding: ActivityCapturePhotoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCapturePhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        if (bundle != null) {
            val bitmap = bundle.getParcelable<Bitmap>("id")
            if (bitmap != null) {
                img.setImageBitmap(bitmap)
            }
        }
//        capturePhotoViewModel = ViewModelProvider(this)[CapturePhotoViewModel::class.java]
        capturePhotoViewModel.tempLiveData.observe(this) {
            binding.tvWeather.text = "temp: $it"

        }
        capturePhotoViewModel.getCurrentWeather()
        capturePhotoViewModel.saveImageInDbLiveData.observe(this) { isImageSaved ->
            if (isImageSaved) {
                finish()
            } else {
                Toast.makeText(this, "Can Not Save Image", Toast.LENGTH_LONG).show()
            }
        }
        binding.fbDonePhoto.setOnClickListener {
            capturePhotoViewModel.saveImageInDb()
            getScreenShotFromView(constraintView)

        }
    }

    private fun saveMediaToStorage(bitmap: Bitmap) {
        // Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        // Output stream
        var fos: OutputStream? = null

        // For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // getting the contentResolver
            this.contentResolver?.also { resolver ->

                // Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    // putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                // Inserting the contentValues to
                // contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                // Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            // These for devices running on android < Q
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            // Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this, "Captured View and saved to Gallery", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getScreenShotFromView(v: View): Bitmap? {
        // create a bitmap object
        var screenshot: Bitmap? = null
        try {

            screenshot =
                Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(screenshot)
            v.draw(canvas)
        } catch (e: Exception) {
            Log.e("GFG", "Failed to capture screenshot because:" + e.message)
        }
        return screenshot
    }

}




