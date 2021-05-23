package edu.sjsu.nutritionfinder.ui.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import edu.sjsu.nutritionfinder.R
import edu.sjsu.nutritionfinder.databinding.LayoutFragmentHomeBinding
import edu.sjsu.nutritionfinder.viewmodels.HomeFragmentViewModel
import java.io.File

class HomeFragment : Fragment() {

    private lateinit var dataBinding: LayoutFragmentHomeBinding
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var progressDialog: AlertDialog
    private val observer = Observer<String?> {
        when (it) {
            null -> {
                progressDialog.dismiss()
                AlertDialog.Builder(this@HomeFragment.context)
                    .setMessage("Some Error Occurred. Please try again later.")
            }

            else -> {
                progressDialog.dismiss()
                val bundle = Bundle()
                bundle.putString(FoodItemFragment.KEY_FOOD_ITEM_NAME, it)
                viewModel.tempImageFilePath?.also { filePath ->
                    bundle.putString(FoodItemFragment.KEY_FOOD_ITEM_IMAGE_PATH, filePath)
                }

                findNavController().navigate(R.id.move_to_food_item_details, bundle)
            }
        }
    }

    companion object {
        private const val CAMERA_ACTIVITY_RESULT = 1
        private const val IMAGE_PICKER_RESULT = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_fragment_home,
            container,
            false
        )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = defaultViewModelProviderFactory.create(HomeFragmentViewModel::class.java)
        dataBinding.btnCamera.setOnClickListener {
            launchCamera()
        }
        dataBinding.btnSelectImage.setOnClickListener {
            pickImage()
        }
        progressDialog =
            AlertDialog.Builder(this.activity).setCancelable(false).setView(R.layout.loader)
                .create()
        viewModel.liveDataImageRecognitionResult.observe(viewLifecycleOwner, observer)
        setupAppBar()
    }

    private fun launchCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, CAMERA_ACTIVITY_RESULT)
    }

    private fun pickImage() {
        val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
        imagePickerIntent.type = "image/*"
        val intentWithPicker =
            Intent.createChooser(imagePickerIntent, "Choose an image of Vegetable of Fruit")
        startActivityForResult(intentWithPicker, IMAGE_PICKER_RESULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_ACTIVITY_RESULT -> {
                val file = this.context?.let {
                    viewModel.copyBitmapToFileSystem(
                        data?.extras?.get("data") as? Bitmap?
                    )
                }

                file?.also {
                    startImageRecognition(it)
                }
            }

            IMAGE_PICKER_RESULT -> {
                data?.data?.let {
                    getBitmapFromURI(it)?.also { bitmap ->
                        val file = viewModel.copyBitmapToFileSystem(
                            bitmap
                        )
                        file?.also { file ->
                            startImageRecognition(file)
                        }
                    }
                }
            }
        }
    }

    private fun startImageRecognition(file: File) {
        progressDialog.show()
        viewModel.initiateImageRecognition(file)
    }

    private fun getBitmapFromURI(contentUri: Uri?): Bitmap? {
        return contentUri?.let {
            val fileOpenMode = "r"
            activity?.contentResolver?.openFileDescriptor(contentUri, fileOpenMode)
                ?.let { fileDescriptor ->
                    BitmapFactory.decodeFileDescriptor(fileDescriptor.fileDescriptor)
                }
        }
    }

    private fun setupAppBar() {
        val navController = findNavController()
        NavigationUI.setupWithNavController(dataBinding.toolBarHome, navController)
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.liveDataImageRecognitionResult.removeObserver(observer)
    }
}