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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.networktask.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import com.example.networktask.viewmodel.CacheViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var adaptor: WeatherAdapter
    val cacheViewModel by viewModels<CacheViewModel>()
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerview.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        adaptor = WeatherAdapter()
        binding.recyclerview.adapter = adaptor
        cacheViewModel.imageLiveData.observe(this){
                adaptor.submitList(it)
        }
        ItemTouchHelper(
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    cacheViewModel.deleteImage(adaptor.getImageAT(viewHolder.adapterPosition))
                }

            }).attachToRecyclerView(binding.recyclerview)






        fb_add_photo.setOnClickListener {
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhoto.launch(callCameraIntent)
        }
        permissionCamera.launch(Manifest.permission.CAMERA)

    }

    override fun onResume() {
        super.onResume()
        cacheViewModel.getImages()
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
//            if (it) {
//                Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(applicationContext, "Permission not granted", Toast.LENGTH_LONG)
//                    .show()
//
//            }

        }


}



