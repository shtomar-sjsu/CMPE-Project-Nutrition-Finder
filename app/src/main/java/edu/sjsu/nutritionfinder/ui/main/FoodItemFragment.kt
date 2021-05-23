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
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import edu.sjsu.nutritionfinder.R
import edu.sjsu.nutritionfinder.databinding.LayoutFragmentFoodItemBinding
import edu.sjsu.nutritionfinder.models.Nutrient
import edu.sjsu.nutritionfinder.models.NutritionInfo
import edu.sjsu.nutritionfinder.viewmodels.FoodItemViewModel
import java.util.*

class FoodItemFragment : Fragment() {

    companion object {
        const val KEY_FOOD_ITEM_NAME = "imageName"
        const val KEY_FOOD_ITEM_IMAGE_PATH = "imagePath"
    }

    private lateinit var dataBinding: LayoutFragmentFoodItemBinding
    lateinit var viewModel: FoodItemViewModel
    private val dataObserver = Observer<NutritionInfo?> {
        if (it == null) {
            AlertDialog.Builder(activity)
                .setMessage("Some Error Occurred in fetching Nutrition Info").create().show()
        } else {
            val bundle = Bundle()
            bundle.putParcelableArrayList(
                NutritionDetailsFragment.NUTRITION_LIST_KEY,
                it.foods[0].nutrientsList as ArrayList<out Parcelable>
            )
            arguments?.also { args ->
                bundle.putString(
                    NutritionDetailsFragment.FOOD_ITEM_NAME_KEY,
                    args.getString(KEY_FOOD_ITEM_NAME)
                )
            }
            findNavController().navigate(R.id.move_to_nutrition_Details, bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.layout_fragment_food_item, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = defaultViewModelProviderFactory.create(FoodItemViewModel::class.java)
        setupToolBar()
        dataBinding.veggieName =
            "Food item detected: ${arguments?.getString(KEY_FOOD_ITEM_NAME) ?: "Error occurred!!"}"
        dataBinding.imageSrc = arguments?.getString(KEY_FOOD_ITEM_IMAGE_PATH) ?: ""
        dataBinding.showNutrition.setOnClickListener {
            arguments?.getString(KEY_FOOD_ITEM_NAME)?.let {
                viewModel.fetchNutritionDetails(it)
            }
        }
        viewModel.liveDataNutritionInfo.observe(viewLifecycleOwner, dataObserver)
    }

    private fun setupToolBar() {
        val navController = findNavController()
        NavigationUI.setupWithNavController(dataBinding.toolBarFoodItem, navController)
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.liveDataNutritionInfo.removeObserver(dataObserver)
    }
}
