package edu.sjsu.nutritionfinder.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import edu.sjsu.nutritionfinder.models.NutritionInfo
import edu.sjsu.nutritionfinder.networking.Networking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodItemViewModel(application: Application) : AndroidViewModel(application) {

    val liveDataNutritionInfo: MutableLiveData<NutritionInfo?> by lazy {
        MutableLiveData()
    }

    fun fetchNutritionDetails(veggieName: String) {
        Networking.nutritionDetailsService.getNutritionDetails(veggieName)
            .enqueue(object : Callback<NutritionInfo?> {

                override fun onResponse(
                    call: Call<NutritionInfo?>,
                    response: Response<NutritionInfo?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataNutritionInfo.value = response.body()
                    } else {
                        liveDataNutritionInfo.value = null
                    }
                }

                override fun onFailure(call: Call<NutritionInfo?>, t: Throwable) {
                    liveDataNutritionInfo.value = null
                }

            })
    }
}