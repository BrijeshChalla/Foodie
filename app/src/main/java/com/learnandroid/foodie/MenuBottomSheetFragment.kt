package com.learnandroid.foodie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.learnandroid.foodie.adapter.MenuAdapter
import com.learnandroid.foodie.databinding.FragmentMenuBottomSheetBinding

class MenuBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMenuBottomSheetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)

        binding.btnBackMenu.setOnClickListener {
            dismiss()
        }
        val menuFoodName = listOf(
            "Burger",
            "Momos",
            "Manchurian",
            "Pizza",
            "Burger",
            "Momos",
            "Manchurian",
            "Pizza"
        )
        val menuPrice = listOf("₹200", "₹150", "₹300", "₹500", "₹200", "₹150", "₹300", "₹500")
        val menuItemImg = listOf(
            R.drawable.menu1,
            R.drawable.menu2,
            R.drawable.menu3,
            R.drawable.menu4,
            R.drawable.menu1,
            R.drawable.menu2,
            R.drawable.menu3,
            R.drawable.menu4
        )
        val adapter =
            MenuAdapter(ArrayList(menuFoodName), ArrayList(menuPrice), ArrayList(menuItemImg))
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
        return binding.root
    }

    companion object {

    }
}