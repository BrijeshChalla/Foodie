package com.learnandroid.foodie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.learnandroid.foodie.adapter.NotificationAdapter
import com.learnandroid.foodie.databinding.FragmentNotificationBottomBinding

class Notification_Bottom_Fragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBottomBinding.inflate(layoutInflater, container, false)
        val notification = listOf(
            "Your order has been Canceled",
            "Order has been taken by the Driver",
            "Congratulation your order Placed!"
        )
        val notificationImage =
            listOf(R.drawable.sademoji, R.drawable.truck, R.drawable.congrats)

        val adapter = NotificationAdapter(ArrayList(notification), ArrayList(notificationImage))
        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationRecyclerView.adapter = adapter
        return binding.root
    }

    companion object {

    }
}