package com.learnandroid.foodie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learnandroid.foodie.databinding.PopularItemBinding

class PopularAdapter (private val items:List<String>,private val price:List<String>, private val image:List<Int>) : RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val item = items[position]
        val images = image[position]
        val price = price[position]
        holder.bind(item, price, images)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class PopularViewHolder (private val binding: PopularItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val imagesView = binding.imgFoodIcon
        fun bind(item: String,price: String, images: Int) {
            binding.txtFoodName.text = item
            binding.pricePopular.text =  price
            imagesView.setImageResource(images)
        }

    }
}