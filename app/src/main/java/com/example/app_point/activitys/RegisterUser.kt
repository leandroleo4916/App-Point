package com.example.app_point.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.app_point.R
import com.example.app_point.repository.ReposiitoryUser
import kotlinx.android.synthetic.main.register_user.*

class RegisterUser : AppCompatActivity(), View.OnClickListener {

    private val mReposiitoryUser: ReposiitoryUser = ReposiitoryUser(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_user)

        listener()
    }

    private fun listener(){
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
        val name = edittext_username.text.toString()
        val email = edittext_email.text.toString()
        val senha = edittext_senha.text.toString()
        val confirmeSenha = edittext_confirme_senha.text.toString()

        when {
            name == "" || email == "" || senha == "" || confirmeSenha == "" -> {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
            senha != confirmeSenha -> {
                Toast.makeText(this, "As senhas estão diferentes!", Toast.LENGTH_SHORT).show()
            }
            mReposiitoryUser.getUser(name, email, senha)->{
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}