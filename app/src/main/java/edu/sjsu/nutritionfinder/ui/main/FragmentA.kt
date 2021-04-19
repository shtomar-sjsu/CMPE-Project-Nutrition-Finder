package edu.sjsu.nutritionfinder.ui.main

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.graphics.BitmapCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.amazonaws.services.rekognition.AmazonRekognitionClient
import edu.sjsu.nutritionfinder.R
import edu.sjsu.nutritionfinder.databinding.LayoutFragmentABinding
import edu.sjsu.nutritionfinder.viewmodels.FragmentAViewModel
import java.io.FileOutputStream

class FragmentA : Fragment() {

    private lateinit var dataBinding: LayoutFragmentABinding
    private lateinit var viewModel: FragmentAViewModel
    private lateinit var progressDialog: AlertDialog
    private val observer = Observer<String?> {
        when (it) {
            null -> {
                progressDialog.dismiss()
                AlertDialog.Builder(this@FragmentA.context)
                    .setMessage("Some Error Occured. Please try again later.")
            }

            else -> {
                progressDialog.dismiss()
                val bundle = Bundle()
                bundle.putString("imageName", it)
                findNavController().navigate(R.id.move_to_b, bundle)
            }
        }
    }

    companion object {
        private val CAMERA_ACTIVITY_RESULT = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_fragment_a,
            container,
            false
        )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = defaultViewModelProviderFactory.create(FragmentAViewModel::class.java)
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