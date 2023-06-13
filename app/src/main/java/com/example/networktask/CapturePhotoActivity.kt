package com.example.networktask

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.networktask.databinding.ActivityCapturePhotoBinding
import com.example.networktask.viewmodel.CapturePhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_capture_photo.*
import javax.inject.Inject

@AndroidEntryPoint
class CapturePhotoActivity : AppCompatActivity() {
    @Inject
    lateinit var capturePhotoViewModel: CapturePhotoViewModel

    private lateinit var binding: ActivityCapturePhotoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCapturePhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        if (bundle != null) {
//            val image = bundle.get("data") as Bitmap?
            val bitmap = bundle.getParcelable<Bitmap>("id")
            if (bitmap != null) {
                img.setImageBitmap(bitmap)
            }
        }
        capturePhotoViewModel = ViewModelProvider(this)[CapturePhotoViewModel::class.java]
        capturePhotoViewModel.tempLiveData.observe(this) {
            binding.tvWeather.text = "temp: $it"
            getScreenShotFromView(binding.cardView)

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
        }
    }


    private fun getScreenShotFromView(v: View): Bitmap? {
        // create a bitmap object
        var screenshot: Bitmap? = null
        try {
            // inflate screenshot object
            // with Bitmap.createBitmap it
            // requires three parameters
            // width and height of the view and
            // the background color
            screenshot =
                Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            // Now draw this bitmap on a canvas
            val canvas = Canvas(screenshot)
            v.draw(canvas)
        } catch (e: Exception) {
            Log.e("GFG", "Failed to capture screenshot because:" + e.message)
        }
        // return the bitmap
        return screenshot
    }


}


