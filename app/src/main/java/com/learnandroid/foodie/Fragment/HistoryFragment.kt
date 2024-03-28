package com.learnandroid.foodie.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.learnandroid.foodie.RecentOrderItem
import com.learnandroid.foodie.adapter.BuyAgainAdapter
import com.learnandroid.foodie.databinding.FragmentHistoryBinding
import com.learnandroid.foodie.model.OrderDetails

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItem: MutableList<OrderDetails> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment

        // initialize firebase auth
        auth = FirebaseAuth.getInstance()
        // initialize firebase database
        database = FirebaseDatabase.getInstance()

        //Retrieve and display the user order history
        retrieveBuyHistory()

        // recent buy button click
        binding.recentBuyItem.setOnClickListener {
            seeItemsRecentBuy()
        }

        return binding.root
    }

    // function to see items in recent buy
    private fun seeItemsRecentBuy() {
        listOfOrderItem.firstOrNull()?.let { recentBuy ->
            val intent = Intent(requireContext(),RecentOrderItem::class.java)
            intent.putExtra("RecentBuyOrderItem",recentBuy)
            startActivity(intent)
        }
    }

    // function to retrieve items in buy history
    private fun retrieveBuyHistory() {
        binding.recentBuyItem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid ?: ""

        val buyItemReference: DatabaseReference =
            database.reference.child("user").child(userId).child("BuyHistory")
        val shortingQuery = buyItemReference.orderByChild("currentTime")

        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children) {
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItem.add(it)
                    }
                }
                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()) {
                    // display the most recent order details
                    setDataInRecentBuyItem()
                    // setup the recyclerview with previous order details
                    setPreviousBuyItemRecyclerView()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            // function to display the most recent order details
            private fun setDataInRecentBuyItem() {
                binding.recentBuyItem.visibility = View.VISIBLE
                val recentOrderItem = listOfOrderItem.firstOrNull()
                recentOrderItem?.let {
                    with(binding) {
                        txtFoodNameHistory.text = it.foodNames?.firstOrNull() ?: ""
                        txtPriceHistory.text = it.foodPrices?.firstOrNull() ?: ""
                        val image = it.foodImages?.firstOrNull() ?: ""
                        val uri = Uri.parse(image)
                        Glide.with(requireContext()).load(uri).into(imgFoodHistory)

                        listOfOrderItem.reverse()
                        if (listOfOrderItem.isNotEmpty()) {
                        }
                    }
                }
            }
        })

    }
    // function to setup the recyclerview
    private fun setPreviousBuyItemRecyclerView() {
        val buyAgainFoodName = mutableListOf<String>()
        val buyAgainFoodPrice = mutableListOf<String>()
        val buyAgainFoodImage = mutableListOf<String>()
        for (i in 1 until listOfOrderItem.size) {
            listOfOrderItem[i].foodNames?.firstOrNull()?.let {
                buyAgainFoodName.add(it)
                listOfOrderItem[i].foodPrices?.firstOrNull()?.let {
                    buyAgainFoodPrice.add(it)
                    listOfOrderItem[i].foodImages?.firstOrNull()?.let {
                        buyAgainFoodImage.add(it)
                    }
                }
                val rv = binding.buyAgainRecyclerView
                rv.layoutManager = LinearLayoutManager(requireContext())
                buyAgainAdapter = BuyAgainAdapter(buyAgainFoodName,buyAgainFoodPrice,buyAgainFoodImage,requireContext())
                rv.adapter = buyAgainAdapter
            }
        }
    }
}