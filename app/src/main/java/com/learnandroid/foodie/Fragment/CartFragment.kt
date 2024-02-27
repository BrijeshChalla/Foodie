package com.learnandroid.foodie.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnandroid.foodie.PayOutActivity
import com.learnandroid.foodie.R
import com.learnandroid.foodie.adapter.CartAdapter
import com.learnandroid.foodie.databinding.FragmentCartBinding

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        val cartItems = listOf("Burger", "Momos", "Manchurian", "Pizza")
        val itemPriceCart = listOf("₹200", "₹150", "₹300", "₹500")
        val itemImg = listOf(R.drawable.menu1, R.drawable.menu2, R.drawable.menu3, R.drawable.menu4)
        val adapter =
            CartAdapter(ArrayList(cartItems), ArrayList(itemPriceCart), ArrayList(itemImg))
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = adapter

        binding.btnProceed.setOnClickListener {
            val intent = Intent(requireContext(),PayOutActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    companion object {
    }
}