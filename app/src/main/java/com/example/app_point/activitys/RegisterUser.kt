package com.example.app_point.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.app_point.R
import com.example.app_point.business.BusinessUser
import com.example.app_point.databinding.RegisterUserBinding
import org.koin.android.ext.android.inject

class RegisterUser : AppCompatActivity() {

    private val mBusinessUser: BusinessUser by inject()
    private val binding by lazy { RegisterUserBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        listener()
    }

    private fun listener(){
        binding.imageBack.setOnClickListener { finish() }
        binding.textInicio.setOnClickListener { finish() }
        binding.buttomRegisterUser.setOnClickListener { registerUser() }
    }

    private fun registerUser(){
        val name = binding.edittextUsername.text.toString()
        val email = binding.edittextEmail.text.toString()
        val password = binding.edittextSenha.text.toString()
        val confirmPassword = binding.edittextConfirmeSenha.text.toString()

        when {
            name.isEmpty()  -> binding.edittextUsername.error = "Digite Nome"
            email.isEmpty() -> binding.edittextEmail.error = "Digite Email"
            password.isEmpty() -> binding.edittextSenha.error == "Digite Senha"
            confirmPassword.isEmpty() -> binding.edittextConfirmeSenha.error == "Confirme Senha"
            password != confirmPassword -> Toast.makeText(this, getString(R.string.senhas_diferentes),
                Toast.LENGTH_SHORT).show()
            else -> createEmailAndPassword(name, email, password)
        }
    }

    private fun createEmailAndPassword(name: String, email: String, password: String){

        if (mBusinessUser.setUser(name, email, password)){
            startActivity(Intent(this, MainActivity::class.java))
            Toast.makeText(this, getString(R.string.bem_vindo),
                Toast.LENGTH_SHORT).show()
            finish()
        }
        else {
            Toast.makeText(this, getString(R.string.nao_foi_possivel_cadastrar),
                Toast.LENGTH_SHORT).show()
        }
    }
}