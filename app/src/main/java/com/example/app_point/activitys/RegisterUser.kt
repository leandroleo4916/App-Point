package com.example.app_point.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.app_point.R
import com.example.app_point.business.BusinessUser
import com.example.app_point.repository.ReposiitoryUser
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.perfil_funcionario.*
import kotlinx.android.synthetic.main.register_user.*

class RegisterUser : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBusinessUser: BusinessUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_user)

        mBusinessUser = BusinessUser(this)
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

        val editTextName = edittext_username
        val editTextEmail = edittext_email
        val editTextSenha = edittext_senha
        val editTextConfirma = edittext_confirme_senha

        when {
            name == ""  -> {
                editTextName.error = "Digite Nome"
            }
            email == "" -> {
                editTextEmail.error = "Digite Email"
            }
            senha == "" -> {
                editTextSenha.error == "Digite Senha"
            }
            confirmeSenha == "" -> {
                editTextConfirma.error == "Confirme Senha"
            }
            senha != confirmeSenha -> {
                Toast.makeText(this, getString(R.string.senhas_diferentes), Toast.LENGTH_SHORT).show()
            }
            mBusinessUser.getUser(name, email, senha)->{
                Toast.makeText(this, getString(R.string.cadastro_feito), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}