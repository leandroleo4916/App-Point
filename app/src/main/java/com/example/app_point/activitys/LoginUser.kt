package com.example.app_point.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.app_point.R
import com.example.app_point.business.BusinessUser
import kotlinx.android.synthetic.main.activity_login_user.*
import kotlinx.android.synthetic.main.activity_register_user.*

class LoginUser : AppCompatActivity(), View.OnClickListener {

    private val mBusinessUser: BusinessUser = BusinessUser(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)

        listener()
    }

    private fun listener(){
        text_register_user.setOnClickListener(this)
        buttom_login.setOnClickListener(this)
        buttom_jump.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            text_register_user -> startActivity(Intent(this, RegisterUser::class.java))
            buttom_login -> loginUser()
            buttom_jump -> startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun loginUser(){
        val userLogin = user_login.text.toString()
        val userPassword = password_login.text.toString()

        when {
            userLogin == "" || userPassword == "" -> {
                Toast.makeText(this, getString(R.string.preencha_dados), Toast.LENGTH_SHORT).show()
            }
            mBusinessUser.storeUser(userLogin, userPassword) -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            else -> {
                Toast.makeText(this, getString(R.string.erro_usuario), Toast.LENGTH_SHORT).show()
            }
        }
    }
}