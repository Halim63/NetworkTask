package com.example.networktask

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.networktask.cache.Image
import com.example.networktask.databinding.ActivityCapturePhotoBinding
import com.example.networktask.viewmodel.CapturePhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_capture_photo.img
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Arrays


@AndroidEntryPoint
class CapturePhotoActivity : AppCompatActivity() {

    val capturePhotoViewModel by viewModels<CapturePhotoViewModel>()

    private lateinit var binding: ActivityCapturePhotoBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityCapturePhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        callCameraToCapturePhoto()
        permissionStorge.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        permissionStorge.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

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
            onBtnSaveClicked()

        }

    }


    private fun saveMediaToStorage(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { resolver ->

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this, "Captured View and saved to Gallery", Toast.LENGTH_SHORT).show()
        }
    }


    fun onBtnSaveClicked() {
        val bitmapScreenShot = convertViewToBitmap(this, binding.cardView)
        if (bitmapScreenShot != null) {
            saveMediaToStorage(bitmapScreenShot)
        }
        if (bitmapScreenShot == null) {
            Toast.makeText(this, "something_went_wrong", Toast.LENGTH_LONG).show()
            return
        }
        val file = convertBitmapToFile(this, bitmapScreenShot)
        val byteArray = convertFileToByteArray(file)
        capturePhotoViewModel.saveImageInDb(Image(byteArray))

    }

    private fun convertFileToByteArray(file: File): ByteArray {
        return file.readBytes()
    }

    private fun convertBitmapToFile(context: Context, bitmap: Bitmap): File {
        val file = File(context.cacheDir, "img.png")
        file.createNewFile()
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val bitmapData = bos.toByteArray()
        val fos = FileOutputStream(file)
        fos.write(bitmapData)
        fos.flush()
        fos.close()
        return file
    }

    private fun convertViewToBitmap(context: Context, view: View): Bitmap? {
        var screenShot: Bitmap? = null
        try {
            screenShot = Bitmap.createBitmap(
                view.measuredWidth,
                view.measuredHeight,
                Bitmap.Config.ARGB_8888
            )

            val canvas = Canvas(screenShot)
            view.draw(canvas)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            return screenShot
        }
    }


    private fun callCameraToCapturePhoto() {
        val bundle = intent.extras
        if (bundle != null) {
            val bitmap = bundle.getParcelable<Bitmap>("id")
            if (bitmap != null) {
                img.setImageBitmap(bitmap)
            }
        }
    }


    private val permissionStorge =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        {
//            if (it) {
//                Toast.makeText(applicationContext, "Permission s", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(applicationContext, "Permission not s", Toast.LENGTH_LONG)
//                    .show()
//
//            }

        }

}




