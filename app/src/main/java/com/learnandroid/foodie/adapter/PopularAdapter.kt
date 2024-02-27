package com.learnandroid.foodie.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learnandroid.foodie.DetailsActivity
import com.learnandroid.foodie.databinding.PopularItemBinding
import com.learnandroid.foodie.models.Product

class PopularAdapter(private val productList: MutableList<Product>, private val requireContext : Context) :
    RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(
            PopularItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val item = productList[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            val intent = Intent(requireContext, DetailsActivity::class.java)
            intent.putExtra("MenuItemName", item.productName)
            intent.putExtra("MenuItemImage", item.imageUrl)
            requireContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class PopularViewHolder(private val binding: PopularItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imagesView = binding.imgFoodIcon
        fun bind(item: Product) {
            binding.txtFoodName.text = item.productName
            binding.pricePopular.text = item.price
            imagesView.setImageResource(item.imageUrl)
        }

    }
}