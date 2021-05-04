package edu.sjsu.nutritionfinder.models

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

//{
//    "totalHits": 3532,
//    "currentPage": 1,
//    "totalPages": 3532,
//    "pageList": [
//    1,
//    2,
//    3,
//    4,
//    5,
//    6,
//    7,
//    8,
//    9,
//    10
//    ],
//    "foodSearchCriteria": {
//    "query": "broccoli",
//    "generalSearchInput": "broccoli",
//    "pageNumber": 1,
//    "numberOfResultsPerPage": 50,
//    "pageSize": 1,
//    "requireAllWords": false
//},
//    "foods": [
//    {
//        "fdcId": 555389,
//        "description": "BROCCOLI",
//        "lowercaseDescription": "broccoli",
//        "dataType": "Branded",
//        "gtinUpc": "709351891335",
//        "publishedDate": "2019-04-01",
//        "brandOwner": "EAT SMART",
//        "ingredients": "BROCCOLI",
//        "marketCountry": "United States",
//        "foodCategory": "Pre-Packaged Fruit & Vegetables",
//        "allHighlightFields": "<b>Ingredients</b>: <em>BROCCOLI</em>",
//        "score": 963.0286,
//        "foodNutrients": [
//        {
//            "nutrientId": 1004,
//            "nutrientName": "Total lipid (fat)",
//            "nutrientNumber": "204",
//            "unitName": "G",
//            "derivationCode": "LCCD",
//            "derivationDescription": "Calculated from a daily value percentage per serving size measure",
//            "value": 0.0
//        },
//        {
//            "nutrientId": 1087,
//            "nutrientName": "Calcium, Ca",
//            "nutrientNumber": "301",
//            "unitName": "MG",
//            "derivationCode": "LCCD",
//            "derivationDescription": "Calculated from a daily value percentage per serving size measure",
//            "value": 47.0
//        },
//        {
//            "nutrientId": 1089,
//            "nutrientName": "Iron, Fe",
//            "nutrientNumber": "303",
//            "unitName": "MG",
//            "derivationCode": "LCCD",
//            "derivationDescription": "Calculated from a daily value percentage per serving size measure",
//            "value": 0.85
//        },
//        {
//            "nutrientId": 1104,
//            "nutrientName": "Vitamin A, IU",
//            "nutrientNumber": "318",
//            "unitName": "IU",
//            "derivationCode": "LCCD",
//            "derivationDescription": "Calculated from a daily value percentage per serving size measure",
//            "value": 2940.0
//        },
//        {
//            "nutrientId": 1162,
//            "nutrientName": "Vitamin C, total ascorbic acid",
//            "nutrientNumber": "401",
//            "unitName": "MG",
//            "derivationCode": "LCCD",
//            "derivationDescription": "Calculated from a daily value percentage per serving size measure",
//            "value": 91.8
//        },
//        {
//            "nutrientId": 1253,
//            "nutrientName": "Cholesterol",
//            "nutrientNumber": "601",
//            "unitName": "MG",
//            "derivationCode": "LCCD",
//            "derivationDescription": "Calculated from a daily value percentage per serving size measure",
//            "value": 0.0
//        },
//        {
//            "nutrientId": 1258,
//            "nutrientName": "Fatty acids, total saturated",
//            "nutrientNumber": "606",
//            "unitName": "G",
//            "derivationCode": "LCCD",
//            "derivationDescription": "Calculated from a daily value percentage per serving size measure",
//            "value": 0.0
//        },
//        {
//            "nutrientId": 1003,
//            "nutrientName": "Protein",
//            "nutrientNumber": "203",
//            "unitName": "G",
//            "derivationCode": "LCCS",
//            "derivationDescription": "Calculated from value per serving size measure",
//            "value": 3.53
//        },
//        {
//            "nutrientId": 1005,
//            "nutrientName": "Carbohydrate, by difference",
//            "nutrientNumber": "205",
//            "unitName": "G",
//            "derivationCode": "LCCS",
//            "derivationDescription": "Calculated from value per serving size measure",
//            "value": 4.71
//        },
//        {
//            "nutrientId": 1008,
//            "nutrientName": "Energy",
//            "nutrientNumber": "208",
//            "unitName": "KCAL",
//            "derivationCode": "LCCS",
//            "derivationDescription": "Calculated from value per serving size measure",
//            "value": 29.0
//        },
//        {
//            "nutrientId": 2000,
//            "nutrientName": "Sugars, total including NLEA",
//            "nutrientNumber": "269",
//            "unitName": "G",
//            "derivationCode": "LCCS",
//            "derivationDescription": "Calculated from value per serving size measure",
//            "value": 2.35
//        },
//        {
//            "nutrientId": 1079,
//            "nutrientName": "Fiber, total dietary",
//            "nutrientNumber": "291",
//            "unitName": "G",
//            "derivationCode": "LCCS",
//            "derivationDescription": "Calculated from value per serving size measure",
//            "value": 3.5
//        },
//        {
//            "nutrientId": 1093,
//            "nutrientName": "Sodium, Na",
//            "nutrientNumber": "307",
//            "unitName": "MG",
//            "derivationCode": "LCCS",
//            "derivationDescription": "Calculated from value per serving size measure",
//            "value": 29.0
//        },
//        {
//            "nutrientId": 1257,
//            "nutrientName": "Fatty acids, total trans",
//            "nutrientNumber": "605",
//            "unitName": "G",
//            "derivationCode": "LCCS",
//            "derivationDescription": "Calculated from value per serving size measure",
//            "value": 0.0
//        }
//        ]
//    }
//    ],
//    "aggregations": {
//    "dataType": {
//    "Branded": 3233,
//    "Survey (FNDDS)": 278,
//    "SR Legacy": 20,
//    "Foundation": 1
//},
//    "nutrients": {}
//}
//}

data class NutritionInfo(@field:Json(name = "foods") val foods: List<FoodInfo>)

data class FoodInfo(@field:Json(name = "foodNutrients") val nutrientsList: List<Nutrient>)

data class Nutrient(
    @field:Json(name = "nutrientName") val name: String,
    @field:Json(name = "derivationDescription") val description: String,
    @field:Json(name = "unitName") val unitName: String,
    @field:Json(name = "value") val value: Float
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readFloat()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel?.also {
            it.writeString(name)
            it.writeString(description)
            it.writeString(unitName)
            it.writeFloat(value)
        }
    }

    fun getReadableName(): String {
        return "Nutrient: $name"
    }

    fun getReadableValue(): String {
        return "Quantity: $value $unitName"
    }

    fun getReadableDescription(): String {
        return "Description: $description"
    }

    companion object CREATOR : Parcelable.Creator<Nutrient> {
        override fun createFromParcel(parcel: Parcel): Nutrient {
            return Nutrient(parcel)
        }

        override fun newArray(size: Int): Array<Nutrient?> {
            return arrayOfNulls(size)
        }
    }
}
