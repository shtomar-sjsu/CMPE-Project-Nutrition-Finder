package edu.sjsu.nutritionfinder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import edu.sjsu.nutritionfinder.R
import edu.sjsu.nutritionfinder.databinding.NutrientInfoBinding
import edu.sjsu.nutritionfinder.models.Nutrient

class NutritionListAdapter : RecyclerView.Adapter<NutritionListAdapter.NutritionInfoViewHolder>() {

    private lateinit var nutrients: ArrayList<Nutrient>

    fun setNutrients(nutrients: ArrayList<Nutrient>) {
        this.nutrients = nutrients
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutritionInfoViewHolder {
        val dataBinding = DataBindingUtil.inflate<NutrientInfoBinding>(
            LayoutInflater.from(parent.context),
            R.layout.nutrient_info,
            parent,
            false
        )
        return NutritionInfoViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: NutritionInfoViewHolder, position: Int) {
        holder.dataBinding.nutrient = nutrients[position]
    }

    override fun getItemCount(): Int {
        return nutrients.size
    }

    class NutritionInfoViewHolder(dataBinding: NutrientInfoBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
        val dataBinding: NutrientInfoBinding = dataBinding
    }
}