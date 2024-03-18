package com.learnandroid.foodie.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learnandroid.foodie.DetailsActivity
import com.learnandroid.foodie.databinding.MenuItemBinding
import com.learnandroid.foodie.model.MenuItem

class MenuAdapter(
    private val menuItems: List<MenuItem>,
    private val requireContext: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItems.size
    inner class MenuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailActivity(position)
                }
            }
        }

        private fun openDetailActivity(position: Int) {
            val menuItem = menuItems[position]

            // an intent to open detail activity and pass data
            val intent = Intent(requireContext, DetailsActivity::class.java).apply {
                putExtra("MenuItemName", menuItem.foodName)
                putExtra("MenuItemImage", menuItem.foodImage)
                putExtra("MenuItemInfo", menuItem.foodInfo)
                putExtra("MenuItemIngredients", menuItem.foodIngredient)
                putExtra("MenuItemPrice", menuItem.foodPrice)
            }
            // start the detail activity
            requireContext.startActivity(intent)
        }

        // set data into recyclerview items name, price, image
        fun bind(position: Int) {
            val menuItem = menuItems[position]
            binding.apply {
                txtFoodNameMenu.text = menuItem.foodName
                priceMenu.text = menuItem.foodPrice
                val uri = Uri.parse(menuItem.foodImage)
                Glide.with(requireContext).load(uri).into(imgFoodMenu)
            }
        }
    }
}