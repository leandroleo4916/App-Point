package com.example.app_point.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.app_point.R
import com.example.app_point.repository.ReposiitoryUser
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLoginUser : AppCompatActivity(), View.OnClickListener {

    private val mRepositoryUser: ReposiitoryUser = ReposiitoryUser(this)

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

        when {
            userLogin == "" || userPassword == "" -> {
                Toast.makeText(this, getString(R.string.preencha_campos), Toast.LENGTH_SHORT).show()
            }
            mRepositoryUser.storeUser(userLogin, userPassword) -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            else -> {
                Toast.makeText(this, getString(R.string.erro_usuario), Toast.LENGTH_SHORT).show()
            }
        }
    }
}