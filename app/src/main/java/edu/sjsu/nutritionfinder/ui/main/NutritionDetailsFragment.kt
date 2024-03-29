package edu.sjsu.nutritionfinder.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import edu.sjsu.nutritionfinder.R
import edu.sjsu.nutritionfinder.adapters.NutritionListAdapter
import edu.sjsu.nutritionfinder.databinding.FragmentNutritionDetailsBinding

class NutritionDetailsFragment : Fragment() {

    companion object {
        const val NUTRITION_LIST_KEY = "nutritionlist"
        const val FOOD_ITEM_NAME_KEY = "food_item"
    }

    lateinit var dataBinding: FragmentNutritionDetailsBinding
    private var nutritionListAdapter = NutritionListAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_nutrition_details, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolBar()
        dataBinding.btnDone.setOnClickListener {
            findNavController().popBackStack(R.id.home_fragment, false)
        }
        val foodItemName = arguments?.getString(FOOD_ITEM_NAME_KEY) ?: ""
        dataBinding.setTitle("Nutrition Details of $foodItemName")
        setupRecyclerView()
    }

    private fun setUpToolBar() {
        val navController = findNavController()
        NavigationUI.setupWithNavController(dataBinding.toolBarNutritionDetails, navController)
    }

    private fun setupRecyclerView() {
        dataBinding.nutritionRecyclerView.adapter = nutritionListAdapter
        nutritionListAdapter.setNutrients(
            arguments?.getParcelableArrayList(NUTRITION_LIST_KEY) ?: arrayListOf()
        )
        nutritionListAdapter.notifyDataSetChanged()
    }
}