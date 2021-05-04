package edu.sjsu.nutritionfinder.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import edu.sjsu.nutritionfinder.R
import edu.sjsu.nutritionfinder.databinding.LayoutFragmentHomeBinding
import edu.sjsu.nutritionfinder.viewmodels.HomeFragmentViewModel

class HomeFragment : Fragment() {

    private lateinit var dataBinding: LayoutFragmentHomeBinding
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var progressDialog: AlertDialog
    private val observer = Observer<String?> {
        when (it) {
            null -> {
                progressDialog.dismiss()
                AlertDialog.Builder(this@HomeFragment.context)
                    .setMessage("Some Error Occured. Please try again later.")
            }

            else -> {
                progressDialog.dismiss()
                val bundle = Bundle()
                bundle.putString("imageName", it)
                findNavController().navigate(R.id.move_to_food_item_details, bundle)
            }
        }
    }

    companion object {
        private const val CAMERA_ACTIVITY_RESULT = 1
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
        dataBinding.btnNavigate.setOnClickListener {
            launchCamera()
        }
        progressDialog =
            AlertDialog.Builder(this.activity).setCancelable(false).setView(R.layout.loader)
                .create()
        viewModel.liveDataImageRecognitionResult.observe(viewLifecycleOwner, observer)
    }

    private fun launchCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, CAMERA_ACTIVITY_RESULT)
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

                file?.let {
                    progressDialog.show()
                    viewModel.uploadImageToS3(file)
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.liveDataImageRecognitionResult.removeObserver(observer)
    }
}