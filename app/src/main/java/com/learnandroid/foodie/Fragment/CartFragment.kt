package com.learnandroid.foodie.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.learnandroid.foodie.PayOutActivity
import com.learnandroid.foodie.adapter.CartAdapter
import com.learnandroid.foodie.databinding.FragmentCartBinding
import com.learnandroid.foodie.model.CartItems

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodName: MutableList<String>
    private lateinit var foodPrice: MutableList<String>
    private lateinit var foodInfo: MutableList<String>
    private lateinit var foodImage: MutableList<String>
    private lateinit var foodIngredient: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var userId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        retrieveCartItems()

        binding.btnProceed.setOnClickListener {
            // get order items details before proceeding to check out
            getOrderItemsDetails()
        }
        return binding.root
    }

    private fun getOrderItemsDetails() {

        val orderIdReference: DatabaseReference =
            database.reference.child("user").child(userId).child("cartItems")
        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodInfo = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val foodIngredient = mutableListOf<String>()
        // get items Quantities
        val foodQuantities = CartAdapter.getUpdatedItemsQuantities()

        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    // get the cart items to the respective list
                    val orderItems = foodSnapshot.getValue(CartItems::class.java)
                    // add items details into list
                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodPrice?.let { foodPrice.add(it) }
                    orderItems?.foodInfo?.let { foodInfo.add(it) }
                    orderItems?.foodImage?.let { foodImage.add(it) }
                    orderItems?.foodIngredient?.let { foodIngredient.add(it) }
                }
                orderNow(foodName, foodPrice, foodInfo, foodImage, foodIngredient, foodQuantities)
            }

            private fun orderNow(
                foodName: MutableList<String>,
                foodPrice: MutableList<String>,
                foodInfo: MutableList<String>,
                foodImage: MutableList<String>,
                foodIngredient: MutableList<String>,
                foodQuantities: MutableList<Int>
            ) {
                if (isAdded && context != null) {
                    val intent = Intent(requireContext(), PayOutActivity::class.java)
                    intent.putExtra("FoodItemName", foodName as ArrayList<String>)
                    intent.putExtra("FoodItemPrice", foodPrice as ArrayList<String>)
                    intent.putExtra("FoodItemInfo", foodInfo as ArrayList<String>)
                    intent.putExtra("FoodItemImage", foodImage as ArrayList<String>)
                    intent.putExtra("FoodItemIngredient", foodIngredient as ArrayList<String>)
                    intent.putExtra("FoodItemQuantity", foodQuantities as ArrayList<Int>)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    context,
                    "Order Processing Failed Please try Again ",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

    }

    private fun retrieveCartItems() {
        // database reference to the firebase
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""
        val foodReference: DatabaseReference =
            database.reference.child("user").child(userId).child("cartItems")

        //list to store cart items
        foodName = mutableListOf()
        foodPrice = mutableListOf()
        foodInfo = mutableListOf()
        foodImage = mutableListOf()
        foodIngredient = mutableListOf()
        quantity = mutableListOf()

        // fetch data from the database
        foodReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    // get the cartItems object from the child node
                    val cartItems = foodSnapshot.getValue(CartItems::class.java)

                    // add cartItems details to the list
                    cartItems?.foodName?.let { foodName.add(it) }
                    cartItems?.foodPrice?.let { foodPrice.add(it) }
                    cartItems?.foodInfo?.let { foodInfo.add(it) }
                    cartItems?.foodImage?.let { foodImage.add(it) }
                    cartItems?.foodQuantity?.let { quantity.add(it) }
                    cartItems?.foodIngredient?.let { foodIngredient.add(it) }
                }

                setAdapter()
            }

            private fun setAdapter() {
                val adapter = CartAdapter(
                    requireContext(),
                    foodName,
                    foodPrice,
                    foodInfo,
                    foodImage,
                    foodIngredient,
                    quantity
                )
                binding.cartRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.cartRecyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Data not Fetched", Toast.LENGTH_SHORT).show()
            }

        })
    }

    companion object {
    }
}