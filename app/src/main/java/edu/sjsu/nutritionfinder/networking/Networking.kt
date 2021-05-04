package edu.sjsu.nutritionfinder.networking

import edu.sjsu.nutritionfinder.models.NutritionInfo
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object Networking {

    private const val BASE_URL = "https://api.nal.usda.gov"

    val nutritionDetailsService: NutritionDetailsService by lazy {
        val builder = Retrofit.Builder()
        builder.baseUrl(BASE_URL).addConverterFactory(MoshiConverterFactory.create())
        builder.build().create(NutritionDetailsService::class.java)
    }

    interface NutritionDetailsService {

        @GET("/fdc/v1/foods/search?pageSize=1&pageNumber=1&api_key=d0z9oP79nGrD8C29lG6aaQhZFXIv5nNf3ddxAgn3")
        fun getNutritionDetails(@Query("query") vegetableName: String): Call<NutritionInfo?>
    }
}