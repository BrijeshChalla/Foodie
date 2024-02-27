package com.learnandroid.foodie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.learnandroid.foodie.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val foodName = intent.getStringExtra("MenuItemName")
        val foodImage = intent.getIntExtra("MenuItemImage",0)
        binding.txtFoodNameDetails.text = foodName
        binding.imgFoodDetails.setImageResource(foodImage)
        binding.btnBackDetails.setOnClickListener {
            finish()
        }
    }
}