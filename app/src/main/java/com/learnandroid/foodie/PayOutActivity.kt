package com.learnandroid.foodie

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.learnandroid.foodie.databinding.ActivityPayOutBinding
import com.learnandroid.foodie.model.OrderDetails

class PayOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayOutBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var totalAmount: String
    private lateinit var foodItemName: ArrayList<String>
    private lateinit var foodItemPrice: ArrayList<String>
    private lateinit var foodItemInfo: ArrayList<String>
    private lateinit var foodItemImage: ArrayList<String>
    private lateinit var foodItemIngredient: ArrayList<String>
    private lateinit var foodItemQuantity: ArrayList<Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize firebase and user details
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        // set user data
        setUserData()

        // get user details from Intent
        val intent = intent
        foodItemName = intent.getStringArrayListExtra("FoodItemName") ?: arrayListOf()
        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice") ?: arrayListOf()
        foodItemInfo = intent.getStringArrayListExtra("FoodItemInfo") ?: arrayListOf()
        foodItemImage = intent.getStringArrayListExtra("FoodItemImage") ?: arrayListOf()
        foodItemIngredient = intent.getStringArrayListExtra("FoodItemIngredient") ?: arrayListOf()
        foodItemQuantity = intent.getIntegerArrayListExtra("FoodItemQuantity") ?: arrayListOf()

        totalAmount = calculateTotalAmount().toString() + "₹"
        binding.txtTotalAmount.isEnabled = false
        binding.txtTotalAmount.setText(totalAmount)

        binding.btnBackPayout.setOnClickListener {
            finish()
        }

        binding.btnPlaceOrder.setOnClickListener {
            // get data from text view
            name = binding.etNamePayOut.text.toString().trim()
            address = binding.etAddressPayOut.text.toString().trim()
            phone = binding.etPhonePayOut.text.toString().trim()

            if (name.isBlank() || address.isBlank() || phone.isBlank()) {
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show()
            } else {
                placeOrder()
            }
        }
    }

    private fun placeOrder() {
        userId = auth.currentUser?.uid ?: ""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("OrderDetails").push().key
        val orderDetails = OrderDetails(
            userId,
            name,
            calculateTotalAmount().toString(),
            foodItemName,
            foodItemPrice,
            foodItemImage,
            foodItemQuantity,
            address,
            phone,
            time,
            itemPushKey,
            false,
            false
        )
        val orderReference = databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetails).addOnSuccessListener {
            showCongratsBottomSheet()
            removeItemFromCart()
            addOrderToHistory(orderDetails)
        }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("user").child(userId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!)
            .setValue(orderDetails)
            .addOnSuccessListener {
                // Handle success if needed
            }
            .addOnFailureListener {
                // Handle failure if needed
            }
    }

    private fun removeItemFromCart() {
        val cartItemsReference = databaseReference.child("user").child(userId).child("cartItems")
        cartItemsReference.removeValue()
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for (i in 0 until foodItemPrice.size) {
            var price = foodItemPrice[i]
            val lastChar = price.last()
            val priceIntValue = if (lastChar == '₹') {
                price.dropLast(1).toInt()
            } else {
                price.toInt()
            }
            var quantity = foodItemQuantity[i]
            totalAmount += priceIntValue * quantity
        }
        return totalAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        user?.let { currentUser ->
            val userId = currentUser.uid
            val userReference = databaseReference.child("user").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val name = snapshot.child("name").getValue(String::class.java) ?: ""
                        val address = snapshot.child("address").getValue(String::class.java) ?: ""
                        val phone = snapshot.child("phone").getValue(String::class.java) ?: ""
                        binding.etNamePayOut.setText(name)
                        binding.etAddressPayOut.setText(address)
                        binding.etPhonePayOut.setText(phone)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled event if needed
                }
            })
        }
    }

    private fun showCongratsBottomSheet() {
        val bottomSheetDialog = CongratsBottomSheet()
        bottomSheetDialog.show(supportFragmentManager, "CongratsBottomSheet")
    }
}
