package edu.sjsu.nutritionfinder.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.sjsu.nutritionfinder.models.Nutrient

class FragmentBViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        val NUTRITION_DETAILS_URL =
            "https://api.nal.usda.gov/fdc/v1/foods/search?pageSize=1&pageNumber=1&&api_key=d0z9oP79nGrD8C29lG6aaQhZFXIv5nNf3ddxAgn3"
    }

    val liveData by lazy {
        MutableLiveData<List<Nutrient>>()
    }

    interface NutritionDetailsResultHandler {
        fun onError()
    }

    lateinit var nutritionDetailsResultHandler: NutritionDetailsResultHandler

    fun fetchNutritionDetails(veggieName: String) {
    }
}