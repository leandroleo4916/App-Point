package com.example.app_point.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.app_point.R
import com.example.app_point.business.BusinessUser
import com.example.app_point.constants.ConstantsUser
import com.example.app_point.databinding.ActivityLoginBinding
import com.example.app_point.utils.SecurityPreferences
import org.koin.android.ext.android.inject

class ActivityLoginUser : AppCompatActivity() {

    private val mBusinessUser: BusinessUser by inject()
    private val mSecurityPreferences: SecurityPreferences by inject()
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        listener()
        verifyLoggedUser()
    }

    private fun listener(){
        binding.textRegisterUser.setOnClickListener{
            startActivity(Intent(this, RegisterUser::class.java))
        }
        binding.buttomLoginUser.setOnClickListener{ loginUser() }
    }

    private fun verifyLoggedUser(){
        val email = mSecurityPreferences.getStoredString(ConstantsUser.USER.COLUNAS.EMAIL)
        val password = mSecurityPreferences.getStoredString(ConstantsUser.USER.COLUNAS.PASSWORD)

        if (email.isNotEmpty() && password.isNotEmpty()){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun loginUser(){
        val userLogin = binding.edittextUser.text.toString()
        val userPassword = binding.edittextSenha.text.toString()

        when {
            userLogin.isEmpty() -> { binding.edittextUser.error = "Digite Login" }
            userPassword.isEmpty() -> { binding.edittextSenha.error = "Digite Senha" }

            mBusinessUser.storeUser(userLogin, userPassword) -> {
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this, getString(R.string.bem_vindo), Toast.LENGTH_SHORT).show()
                finish()
            }
            else -> {
                Toast.makeText(this, getString(R.string.erro_usuario), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
