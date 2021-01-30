package com.example.app_point.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.app_point.R
import com.example.app_point.business.BusinessUser
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLoginUser : AppCompatActivity(), View.OnClickListener {

    private val mBusinessUser: BusinessUser = BusinessUser(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        listener()
    }

    private fun listener(){
        buttom_login_user.setOnClickListener(this)
        text_register_user.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            text_register_user -> startActivity(Intent(this, RegisterUser::class.java))
            buttom_login_user -> loginUser()
        }
    }

    private fun loginUser(){
        val userLogin = edittext_user.text.toString()
        val userPassword = edittext_senha.text.toString()
        val editTextUser = edittext_user
        val editTextPassword = edittext_senha

        when {
            userLogin == "" -> {
                editTextUser.error = "Digite Login"
            }
            userPassword == "" -> {
                editTextPassword.error = "Digite Senha"
            }
            mBusinessUser.storeUser(userLogin, userPassword) -> {
                startActivity(Intent(this, EmployeeActivity::class.java))
                Toast.makeText(this, getString(R.string.bem_vindo), Toast.LENGTH_SHORT).show()
                finish()
            }
            else -> {
                Toast.makeText(this, getString(R.string.erro_usuario), Toast.LENGTH_SHORT).show()
            }
        }
    }
}