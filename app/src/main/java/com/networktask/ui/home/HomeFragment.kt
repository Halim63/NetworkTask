package com.networktask.ui.home

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
import com.example.networktask.databinding.FragmentHomeBinding
import com.networktask.ui.PhotosAdapter
import com.networktask.repos.imagesRepo.ImageDbEntity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    private lateinit var adaptor: PhotosAdapter
    private val viewModel by viewModels<HomeViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setupObservers()
        requestCameraPermission()

    }

    private fun setupObservers() {
        viewModel.imageDbEntityLiveData.observe(viewLifecycleOwner) { list ->
            adaptor.submitList(list)
        }
    }

    private fun requestCameraPermission() = cameraPermission.launch(Manifest.permission.CAMERA)


    private fun initView() {
        setupPhotosRecyclerView()
        binding.fbAddPhoto.setOnClickListener {
            openCamera()

        }
    }

    private fun setupPhotosRecyclerView() {
        binding.recyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
        adaptor = PhotosAdapter()
        binding.recyclerview.adapter = adaptor
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

            }).attachToRecyclerView(binding.recyclerview)
    }

    private fun deletePhoto(image: ImageDbEntity) = viewModel.deleteImage(image)


    private fun openCamera() {
        val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhoto.launch(callCameraIntent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getImages()
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

