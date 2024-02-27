package com.learnandroid.foodie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.learnandroid.foodie.databinding.ActivityChooseLocationBinding

class ChooseLocationActivity : AppCompatActivity() {
    private val binding: ActivityChooseLocationBinding by lazy {
        ActivityChooseLocationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Remove below code this is for testing
        binding.txtDesignedByChoose.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val locationList = arrayOf("Gujarat", "Maharashtra", "Rajasthan", "Goa")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,locationList)
        val autoCompleteTextView = binding.tvLocationList
        autoCompleteTextView.setAdapter(adapter)
    }
}