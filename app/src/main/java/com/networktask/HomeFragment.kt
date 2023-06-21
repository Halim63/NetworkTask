package com.networktask

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
import androidx.recyclerview.widget.RecyclerView
import com.example.networktask.R
import com.networktask.cache.ImageDbEntity
import com.networktask.viewmodel.HomeViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.fb_add_photo
import kotlinx.android.synthetic.main.fragment_home.recyclerview

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var adaptor: PhotosAdapter
    val homeViewModel by viewModels<HomeViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setupObservers()
        requestCameraPermission()

    }

    private fun setupObservers() {
        homeViewModel.imageDbEntityLiveData.observe(viewLifecycleOwner) {
            adaptor.submitList(it)
        }
    }

    private fun requestCameraPermission() = cameraPermission.launch(Manifest.permission.CAMERA)


    private fun initView() {
        setupPhotosRecyclerView()
        fb_add_photo.setOnClickListener {
            openCamera()

        }
    }

    private fun setupPhotosRecyclerView() {
        recyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
        adaptor = PhotosAdapter()
        recyclerview.adapter = adaptor
        setupRecyclerViewSwapActions()

    }

    private fun setupRecyclerViewSwapActions() {
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
                    deletePhoto(adaptor.currentList[viewHolder.adapterPosition])
                }

            }).attachToRecyclerView(recyclerview)
    }

    private fun deletePhoto(image: ImageDbEntity) = homeViewModel.deleteImage(image)


    private fun openCamera() {
        val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhoto.launch(callCameraIntent)
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getImages()
    }

    private val takePhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                findNavController().navigate(
                    R.id.action_homeFragment_to_capturePhotoFragment,
                    bundleOf("id" to imageBitmap)
                )
            }
        }


    private val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {}


}

