package com.learnandroid.foodie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.learnandroid.foodie.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Remove below code this is for testing
        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, ChooseLocationActivity::class.java)
            startActivity(intent)
        }

        binding.btnAlreadyHave.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}