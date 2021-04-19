package edu.sjsu.nutritionfinder.models

import android.os.Parcel
import android.os.Parcelable

data class Nutrient(
    val name: String,
    val description: String,
    val unitName: String,
    val value: Float
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
