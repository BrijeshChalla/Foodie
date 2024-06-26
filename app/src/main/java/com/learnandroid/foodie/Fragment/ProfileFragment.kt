package com.learnandroid.foodie.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.learnandroid.foodie.LoginActivity
import com.learnandroid.foodie.databinding.FragmentProfileBinding
import com.learnandroid.foodie.model.UserModel


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        setUserData()

        binding.apply {
            etNameProfile.isEnabled = false
            etAddressProfile.isEnabled = false
            etEmailProfile.isEnabled = false
            etPhoneProfile.isEnabled = false
            binding.btnEditProfile.setOnClickListener {
                etNameProfile.isEnabled = !etNameProfile.isEnabled
                etAddressProfile.isEnabled = !etAddressProfile.isEnabled
                etEmailProfile.isEnabled = !etEmailProfile.isEnabled
                etPhoneProfile.isEnabled = !etPhoneProfile.isEnabled
            }
        }

        binding.btnSaveProfile.setOnClickListener {
            val name = binding.etNameProfile.text.toString()
            val email = binding.etEmailProfile.text.toString()
            val address = binding.etAddressProfile.text.toString()
            val phone = binding.etPhoneProfile.text.toString()

            updateUserData(name, email, address, phone)
        }

        binding.btnLogOutProfile.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return binding.root
    }

    private fun updateUserData(name: String, email: String, address: String, phone: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.getReference("user").child(userId)
            val userData = hashMapOf(
                "name" to name,
                "address" to address,
                "email" to email,
                "phone" to phone
            )
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile Update Successfully😊", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Profile not Updated☹️ ", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.getReference("user").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        if (userProfile != null) {
                            binding.etNameProfile.setText(userProfile.name)
                            binding.etAddressProfile.setText(userProfile.address)
                            binding.etPhoneProfile.setText(userProfile.phone)
                            binding.etEmailProfile.setText(userProfile.email)
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