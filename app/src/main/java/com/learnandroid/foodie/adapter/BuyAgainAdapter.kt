package com.learnandroid.foodie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learnandroid.foodie.databinding.BuyAgainItemBinding

class BuyAgainAdapter(
    private val buyAgainFoodName: ArrayList<String>,
    private val buyAgainFoodPrice: ArrayList<String>,
    private val buyAgainFoodImg: ArrayList<Int>
) :
    RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {

    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        holder.bind(buyAgainFoodName[position],buyAgainFoodPrice[position],buyAgainFoodImg[position])
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding =
            BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuyAgainViewHolder(binding)
    }

    override fun getItemCount(): Int = buyAgainFoodName.size
    class BuyAgainViewHolder(private val binding: BuyAgainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(foodName: String, foodPrice: String, foodImg: Int) {
            binding.txtFoodNameBuyAgain.text = foodName
            binding.txtPriceBuyAgain.text = foodPrice
            binding.imgFoodBuyAgain.setImageResource(foodImg)
        }

    }
}