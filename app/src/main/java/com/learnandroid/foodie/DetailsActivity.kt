package com.learnandroid.foodie

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.learnandroid.foodie.databinding.ActivityDetailsBinding
import com.learnandroid.foodie.model.CartItems

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private var foodName: String? = null
    private var foodPrice: String? = null
    private var foodInfo: String? = null
    private var foodImage: String? = null
    private var foodIngredient: String? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialize Firebase auth
        auth = FirebaseAuth.getInstance()
        
        foodName = intent.getStringExtra("MenuItemName")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodInfo = intent.getStringExtra("MenuItemInfo")
        foodIngredient = intent.getStringExtra("MenuItemIngredients")
        foodImage = intent.getStringExtra("MenuItemImage")

        with(binding) {
            txtFoodNameDetails.text = foodName
            txtFoodDetails.text = foodInfo
            txtIngredientsDetails.text = foodIngredient
            Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(imgFoodDetails)
        }

        binding.btnBackDetails.setOnClickListener {
            finish()
        }
        binding.btnAddToCartDetails.setOnClickListener {
            addItemToCart()
        }
    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""
        // create a cartItems Object
        val cartItem = CartItems(foodName.toString(),foodPrice.toString(), foodInfo.toString(), foodImage.toString(), foodQuantity = 1)

        // save data to cart item to firebase database
        database.child("user").child(userId).child("cartItems").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this,"Item Added to Cart🛒",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this,"item Not Added☹️",Toast.LENGTH_SHORT).show()
        }
    }
}