package com.learnandroid.foodie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.learnandroid.foodie.databinding.ActivityPayOutBinding

class PayOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayOutBinding

    private lateinit var auth : FirebaseAuth
    private lateinit var name : String
    private lateinit var address : String
    private lateinit var phone : String
    private lateinit var foodItemName : ArrayList<String>
    private lateinit var foodItemPrice : ArrayList<String>
    private lateinit var foodItemInfo : ArrayList<String>
    private lateinit var foodItemImage : ArrayList<String>
    private lateinit var foodItemIngredient : ArrayList<String>
    private lateinit var foodItemQuantity : ArrayList<Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize firebase and user details
        auth =  FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()
        // set user data
        setUserData()

        binding.btnPlaceOrder.setOnClickListener {
            val bottomSheetDialog = CongratsBottomSheet()
            bottomSheetDialog.show(supportFragmentManager,"Test")
        }
    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user!=null){
            val userId = user.uid
            val userReference = databaseReference.child("user").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){
                        val name = snapshot.child("name").getValue(String::class.java)?:""
                        val address = snapshot.child("address").getValue(String::class.java)?:""
                        val phone = snapshot.child("phone").getValue(String::class.java)?:""
                        binding.apply {
                            etNamePayOut.setText(name)
                            etAddressPayOut.setText(address)
                            etPhonePayOut.setText(phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}