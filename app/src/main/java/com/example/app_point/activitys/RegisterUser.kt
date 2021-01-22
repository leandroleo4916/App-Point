package com.example.app_point.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.app_point.R
import kotlinx.android.synthetic.main.register_user.*

class RegisterUser : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_user)

        listener()
    }

    fun listener(){
        image_back.setOnClickListener(this)
        text_inicio.setOnClickListener(this)
        buttom_register_user.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            image_back -> finish()
            text_inicio -> finish()
            buttom_register_user -> registerUser()
        }
    }

    private fun registerUser(){

    }

}