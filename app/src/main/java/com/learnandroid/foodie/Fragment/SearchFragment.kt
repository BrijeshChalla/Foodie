package com.learnandroid.foodie.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.learnandroid.foodie.adapter.MenuAdapter
import com.learnandroid.foodie.databinding.FragmentSearchBinding
import com.learnandroid.foodie.model.MenuItem

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter
    private lateinit var database: FirebaseDatabase
    private val originalMenuItem = mutableListOf<MenuItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        //retrieve item from database
        retrieveMenuItem()
        //setup for search view
        setUpSearchView()

        return binding.root
    }

    private fun retrieveMenuItem() {
        //get database reference
        database = FirebaseDatabase.getInstance()
        // reference to the menu node
        val foodReference: DatabaseReference = database.reference.child("menu")
        foodReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let {
                        originalMenuItem.add(it)
                    }
                }
                showAllMenu()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event if needed
            }
        })
    }

    private fun showAllMenu() {
        val filteredMenuItem = ArrayList(originalMenuItem)
        setAdapter(filteredMenuItem)
    }

    private fun setAdapter(filteredMenuItem: List<MenuItem>) {
        adapter = MenuAdapter(filteredMenuItem, requireContext())
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRecyclerView.adapter = adapter
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
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
        val filteredMenuItem = originalMenuItem.filter {
            it.foodName?.contains(query, ignoreCase = true) == true
        }
        setAdapter(filteredMenuItem)
    }
}
