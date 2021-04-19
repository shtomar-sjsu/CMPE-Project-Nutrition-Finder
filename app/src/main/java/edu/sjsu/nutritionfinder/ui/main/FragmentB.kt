package edu.sjsu.nutritionfinder.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import edu.sjsu.nutritionfinder.R
import edu.sjsu.nutritionfinder.databinding.LayoutFragmentBBinding
import edu.sjsu.nutritionfinder.models.Nutrient
import edu.sjsu.nutritionfinder.viewmodels.FragmentBViewModel
import java.util.ArrayList

class FragmentB : Fragment() {

    lateinit var dataBinding: LayoutFragmentBBinding
    lateinit var viewModel: FragmentBViewModel
    private val dataObserver = Observer<List<Nutrient>?> {
        if (it == null) {
            AlertDialog.Builder(activity)
                .setMessage("Some Error Occurred in fetching Nutrition Info").create().show()
        } else {
            val bundle = Bundle()
            bundle.putParcelableArrayList(
                NutritionDetailsFragment.NUTRITION_LIST_KEY,
                it as ArrayList<out Parcelable>
            )
            findNavController().navigate(R.id.move_to_nutrition_Details, bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.layout_fragment_b, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = defaultViewModelProviderFactory.create(FragmentBViewModel::class.java)
        dataBinding.veggieName =
            "Food item detected: ${arguments?.getString("imageName") ?: "Error occurred!!"}"
        dataBinding.showNutrition.setOnClickListener {
            arguments?.getString("imageName")?.let {
                viewModel.fetchNutritionDetails(it)
            }
        }
        viewModel.liveDataNutritionInfo.observe(viewLifecycleOwner, dataObserver)
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.liveDataNutritionInfo.removeObserver(dataObserver)
    }
}
