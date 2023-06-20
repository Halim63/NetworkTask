package com.example.networktask

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.networktask.viewmodel.CacheViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.fb_add_photo
import kotlinx.android.synthetic.main.fragment_home.recyclerview

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var adaptor: PhotosAdapter
    val cacheViewModel by viewModels<CacheViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
        adaptor = PhotosAdapter()
        recyclerview.adapter = adaptor
        cacheViewModel.imageDbEntityLiveData.observe(viewLifecycleOwner) {
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

            }).attachToRecyclerView(recyclerview)






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
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val action = HomeFragmentDirections.actionHomeFragmentToCapturePhotoFragment()
                val imageBitmap = result.data?.extras?.get("data") as Bitmap

                findNavController().navigate(
                    R.id.action_homeFragment_to_capturePhotoFragment,
                    bundleOf("id" to imageBitmap)
                )
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

