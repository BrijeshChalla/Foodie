package com.learnandroid.foodie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.learnandroid.foodie.databinding.ActivityChooeseLocationBinding

class ChooeseLocationActivity : AppCompatActivity() {
    private val binding: ActivityChooeseLocationBinding by lazy {
        ActivityChooeseLocationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val locationList = arrayOf("Gujarat", "Maharashtra", "Rajasthan", "Goa")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,locationList)
        val autoCompleteTextView = binding.tvLocationList
        autoCompleteTextView.setAdapter(adapter)
    }
}