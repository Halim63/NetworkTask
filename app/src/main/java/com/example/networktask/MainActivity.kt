package com.example.networktask

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.networktask.cache.Image
import com.example.networktask.databinding.ActivityMainBinding
import com.example.networktask.remote.ApiInstance
import kotlinx.android.synthetic.main.activity_main.*
import com.example.networktask.remote.WeatherRepository
import com.example.networktask.viewmodel.CacheViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_capture_photo.img
import kotlinx.android.synthetic.main.list_item.rv_img
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var cacheViewModel: CacheViewModel
    private lateinit var adaptor: WeatherAdapter
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        adaptor = WeatherAdapter()
        binding.recyclerview.adapter = adaptor
        cacheViewModel = ViewModelProvider(this)[CacheViewModel::class.java]
        cacheViewModel.imageLiveData.observe(this){
                adaptor.submitList(it)
        }

            cacheViewModel.getImages()




        fb_add_photo.setOnClickListener {
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhoto.launch(callCameraIntent)
        }
        permissionCamera.launch(Manifest.permission.CAMERA)

    }

    private val takePhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent = Intent(this@MainActivity, CapturePhotoActivity::class.java)
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                intent.putExtra("id", imageBitmap)
                startActivity(intent)
            }
        }


    private val permissionCamera =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        {
            if (it) {
                Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext, "Permission not granted", Toast.LENGTH_LONG)
                    .show()

            }

        }

}



