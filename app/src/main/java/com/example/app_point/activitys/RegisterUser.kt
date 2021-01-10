package com.example.app_point.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.app_point.R
import com.example.app_point.business.BusinessUser
import com.example.app_point.repository.ReposiitoryUser
import kotlinx.android.synthetic.main.activity_login_user.*
import kotlinx.android.synthetic.main.activity_register_user.*

class RegisterUser : AppCompatActivity(), View.OnClickListener {

    private val mRepositoryUser: ReposiitoryUser = ReposiitoryUser(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        listener()
    }
    private fun listener(){
        buttom_save.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when(view){
            buttom_save -> saveUser()
        }
    }
    private fun saveUser(){
        val name = user_login_register.text.toString()
        val email = email_user.text.toString()
        val password = password_user.text.toString()

        when{
            name == "" || email == "" || password == "" -> {
                Toast.makeText(this, getString(R.string.preencha_dados), Toast.LENGTH_SHORT).show()
            }
            mRepositoryUser.getUser(name, email, password) -> {
                Toast.makeText(this, getString(R.string.cadastro_feito), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
            else -> {
                Toast.makeText(this, getString(R.string.erro_inesperado), Toast.LENGTH_SHORT).show()
            }
        }

    }
}