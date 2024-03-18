package com.learnandroid.foodie.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.search.SearchView
import com.learnandroid.foodie.R
import com.learnandroid.foodie.adapter.MenuAdapter
import com.learnandroid.foodie.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter

    private val originalMenuFoodName = listOf(
        "Burger",
        "Momos",
        "Manchurian",
        "Pizza",
        "Burger",
        "Momos",
        "Manchurian",
        "Pizza"
    )

    private val originalMenuPrice = listOf(
        "₹200", "₹150", "₹300", "₹500",
        "₹200", "₹150", "₹300", "₹500"
    )

    private val originalMenuItemImg = listOf(
        R.drawable.menu1, R.drawable.menu2, R.drawable.menu3, R.drawable.menu4,
        R.drawable.menu1, R.drawable.menu2, R.drawable.menu3, R.drawable.menu4
    )

    private val filteredMenuFoodName = mutableListOf<String>()
    private val filteredMenuItemPrice = mutableListOf<String>()
    private val filteredMenuImage = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        binding = FragmentSearchBinding.inflate(inflater, container, false)
//        adapter = MenuAdapter(filteredMenuFoodName, filteredMenuItemPrice, filteredMenuImage, requireContext())
//        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.searchRecyclerView.adapter = adapter

        setUpSearchView()
        // show all menu items
        showAllMenu()

        return binding.root
    }

    private fun showAllMenu() {
        filteredMenuFoodName.clear()
        filteredMenuItemPrice.clear()
        filteredMenuImage.clear()

        filteredMenuFoodName.addAll(originalMenuFoodName)
        filteredMenuItemPrice.addAll(originalMenuPrice)
        filteredMenuImage.addAll(originalMenuItemImg)

        adapter.notifyDataSetChanged()
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object :OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterMenuItems(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterMenuItems(newText.orEmpty())
                return true
            }

        })
    }

    private fun filterMenuItems(query: String) {
        filteredMenuFoodName.clear()
        filteredMenuItemPrice.clear()
        filteredMenuImage.clear()

        query.let { q ->
            originalMenuFoodName.forEachIndexed { index, foodName ->
                if (foodName.contains(q, ignoreCase = true)) {
                    filteredMenuFoodName.add(foodName)
                    filteredMenuItemPrice.add(originalMenuPrice[index])
                    filteredMenuImage.add(originalMenuItemImg[index])
                }
            }
        }

        adapter.notifyDataSetChanged()
    }
}
