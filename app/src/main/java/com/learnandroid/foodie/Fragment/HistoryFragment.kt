package com.learnandroid.foodie.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnandroid.foodie.R
import com.learnandroid.foodie.adapter.BuyAgainAdapter
import com.learnandroid.foodie.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        setUpRecyclerView()
        return binding.root
    }

    private fun setUpRecyclerView(){
        val buyAgainFoodName = arrayListOf("Burger", "Veg. Momo", "Manchurian")
        val buyAgainFoodPrice = arrayListOf("₹150", "₹500", "₹200")
        val buyAgainFoodImg = arrayListOf(R.drawable.menu1, R.drawable.menu2, R.drawable.menu3)

        buyAgainAdapter = BuyAgainAdapter(buyAgainFoodName, buyAgainFoodPrice, buyAgainFoodImg)
        binding.buyAgainRecyclerView.adapter = buyAgainAdapter
        binding.buyAgainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {

    }
}