package com.learnandroid.foodie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharedPreference =  getSharedPreferences("FOODIE", Context.MODE_PRIVATE)


        Handler(Looper.getMainLooper()).postDelayed({
            val isFirstTime = sharedPreference.getBoolean("isFirstTime", true)

            if(isFirstTime){
                var editor = sharedPreference.edit()
                editor.putBoolean("isFirstTime",false)
                editor.commit()
                startActivity(Intent(this, StartActivity ::class.java))
            }else{
                startActivity(Intent(this, MainActivity ::class.java))
            }
            finish()
        },2000)
    }


}