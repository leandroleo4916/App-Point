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
        val senha = binding.edittextSenha.text.toString()
        val confirmeSenha = binding.edittextConfirmeSenha.text.toString()

        val editTextName = binding.edittextUsername
        val editTextEmail = binding.edittextEmail
        val editTextSenha = binding.edittextSenha
        val editTextConfirma = binding.edittextConfirmeSenha

        when {
            name == ""  -> editTextName.error = "Digite Nome"
            email == "" -> editTextEmail.error = "Digite Email"
            senha == "" -> editTextSenha.error == "Digite Senha"
            confirmeSenha == "" -> editTextConfirma.error == "Confirme Senha"
            senha != confirmeSenha -> Toast.makeText(this, getString(R.string.senhas_diferentes),
                Toast.LENGTH_SHORT).show()
            else -> createEmailAndPassword(name, email, senha)
        }
    }

    private fun createEmailAndPassword(name: String, email: String, password: String){

        if (mBusinessUser.getUser(name, email, password)){
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