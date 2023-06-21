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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.networktask.cache.ImageDbEntity
import com.example.networktask.viewmodel.CapturePhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_capture_photo.card_View
import kotlinx.android.synthetic.main.fragment_capture_photo.fb_done_photo
import kotlinx.android.synthetic.main.fragment_capture_photo.img
import kotlinx.android.synthetic.main.fragment_capture_photo.tvWeather
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

private const val PHOTO_FILE_NAME = "captured_photo.png"
private const val PHOTO_QUALITY = 100

@AndroidEntryPoint
class CapturePhotoFragment : Fragment() {

    private val capturePhotoViewModel by viewModels<CapturePhotoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_capture_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        requestStoragePermission()
        setupObservers()
        fetchData()


    }

    private fun initView() {
        initCapturedImageView()

        fb_done_photo.setOnClickListener {
            onDoneBtnClicked()

        }
    }

    private fun fetchData() = capturePhotoViewModel.getCurrentWeather()


    private fun setupObservers() {
        setupWeatherObserver()
        setupSaveImageObserver()
    }

    private fun setupSaveImageObserver() {
        capturePhotoViewModel.saveImageInDbLiveData.observe(viewLifecycleOwner) { isImageSaved ->
            if (isImageSaved) {
                findNavController().navigateUp()

            } else {
                Toast.makeText(requireContext(), R.string.can_not_save_image, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun setupWeatherObserver() {
        capturePhotoViewModel.tempLiveData.observe(viewLifecycleOwner) { temp ->
            tvWeather.text = "temp: $temp"

        }
    }

    private fun requestStoragePermission() {
        storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun saveMediaToStorage(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requireContext().contentResolver?.also { resolver ->

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
            bitmap.compress(Bitmap.CompressFormat.JPEG, PHOTO_QUALITY, it)

        }
    }


    private fun onDoneBtnClicked() {
        val bitmapScreenShot = convertViewToBitmap(requireContext(), card_View)
        if (bitmapScreenShot != null) {
            saveMediaToStorage(bitmapScreenShot)
        }
        if (bitmapScreenShot == null) {
            Toast.makeText(requireContext(), "something_went_wrong", Toast.LENGTH_LONG).show()
            return
        }
        val file = convertBitmapToFile(requireContext(), bitmapScreenShot)
        val byteArray = convertFileToByteArray(file)
        capturePhotoViewModel.saveImageInDb(ImageDbEntity(byteArray))

    }

    private fun convertFileToByteArray(file: File): ByteArray {
        return file.readBytes()
    }

    private fun convertBitmapToFile(context: Context, bitmap: Bitmap): File {
        val file = File(context.cacheDir, PHOTO_FILE_NAME)
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


    private fun initCapturedImageView() {
        val bundle = arguments
        if (bundle != null) {
            val bitmap = bundle.getParcelable<Bitmap>("id")
            if (bitmap != null) {
                img.setImageBitmap(bitmap)
            }
        }
    }


    private val storagePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        {}


}