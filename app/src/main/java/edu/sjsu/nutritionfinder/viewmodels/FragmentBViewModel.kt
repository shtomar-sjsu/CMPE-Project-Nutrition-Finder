package edu.sjsu.nutritionfinder.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import edu.sjsu.nutritionfinder.models.Nutrient
import edu.sjsu.nutritionfinder.networking.NetworkingUtils
import org.json.JSONObject

class FragmentBViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        val NUTRITION_DETAILS_URL =
            "https://api.nal.usda.gov/fdc/v1/foods/search?pageSize=1&pageNumber=1&api_key=d0z9oP79nGrD8C29lG6aaQhZFXIv5nNf3ddxAgn3&query="
    }

    val liveDataNutritionInfo: MutableLiveData<List<Nutrient>?> by lazy {
        MutableLiveData()
    }

    interface NutritionDetailsResultHandler {
        fun onError()
    }

    fun fetchNutritionDetails(veggieName: String) {
        NetworkingUtils.asyncConnection(
            "$NUTRITION_DETAILS_URL$veggieName",
            "GET",
            null,
            null,
            object : NetworkingUtils.ResponseHandler {
                override fun onSuccess(response: String?) {
                    response?.let { response ->
                        liveDataNutritionInfo.postValue(getNutrients(veggieName, response))
                    }
                }

                override fun onFailure(
                    failureType: NetworkingUtils.ResponseHandler.FailureType?,
                    responseCode: Int
                ) {
                    liveDataNutritionInfo.postValue(null)
                }

            })
    }

    private fun getNutrients(veggieName: String, json: String): List<Nutrient>? {
        val rootJsonObj = JSONObject(json)
        val foodsJsonArr = rootJsonObj.optJSONArray("foods") ?: return null
        val jsonObject = foodsJsonArr.getJSONObject(0)
        if (!jsonObject.getString("description").equals(veggieName, ignoreCase = true)) {
            return null
        }
        val nutrientsArray = jsonObject.getJSONArray("foodNutrients")
        val nutrientList = arrayListOf<Nutrient>()
        for (i in 0 until nutrientsArray.length()) {
            val obj = nutrientsArray.getJSONObject(i)
            nutrientList.add(
                Nutrient(
                    name = obj.getString("nutrientName"),
                    description = obj.getString("derivationDescription"),
                    unitName = obj.getString("unitName"),
                    value = obj.getDouble("value").toFloat()
                )
            )
        }
        return nutrientList
    }
}