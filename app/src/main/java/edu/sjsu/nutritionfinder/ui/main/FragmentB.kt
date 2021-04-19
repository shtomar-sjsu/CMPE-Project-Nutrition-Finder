package edu.sjsu.nutritionfinder.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.sjsu.nutritionfinder.R
import edu.sjsu.nutritionfinder.databinding.LayoutFragmentBBinding
import edu.sjsu.nutritionfinder.models.Nutrient
import edu.sjsu.nutritionfinder.viewmodels.FragmentBViewModel

class FragmentB : Fragment() {

    lateinit var dataBinding: LayoutFragmentBBinding
    lateinit var viewModel: FragmentBViewModel
    private val dataObserver = Observer<List<Nutrient>> {

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
        dataBinding.veggieName = "Food item detected: ${arguments?.getString("imageName") ?: ""}"
        dataBinding.showNutrition.setOnClickListener {

        }

        viewModel.liveData.observe(viewLifecycleOwner, dataObserver)
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.liveData.removeObserver(dataObserver)
    }
}
