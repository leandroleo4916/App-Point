package com.example.app_point.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.app_point.R

class MainActivityPrincipal : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_principal)

        val back = onBackPressedDispatcher

    }
}