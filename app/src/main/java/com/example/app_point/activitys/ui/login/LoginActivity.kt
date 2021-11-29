package com.example.app_point.activitys.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_point.R
import com.example.app_point.activitys.ui.activity.MainActivityController
import com.example.app_point.constants.ConstantsUser
import com.example.app_point.databinding.ActivityLoginBinding
import com.example.app_point.utils.SecurityPreferences
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {

    private val businessUser: BusinessUser by inject()
    private val securityPreferences: SecurityPreferences by inject()
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        listener()
        verifyLoggedUser()
    }

    private fun listener(){
        binding.textRegisterUser.setOnClickListener{
            startActivity(Intent(this, RegisterUserActivity::class.java))
        }
        binding.buttomLoginUser.setOnClickListener{ loginUser() }
    }

    private fun verifyLoggedUser(){
        val email = securityPreferences.getStoredString(ConstantsUser.USER.COLUNAS.EMAIL)
        val password = securityPreferences.getStoredString(ConstantsUser.USER.COLUNAS.PASSWORD)

        if (email.isNotEmpty() && password.isNotEmpty()){
            startActivity(Intent(this, MainActivityController::class.java))
            finish()
        }
    }

    private fun loginUser(){

        val userLogin = binding.edittextUser.text.toString()
        val userPassword = binding.edittextSenha.text.toString()

        when {
            userLogin.isEmpty() -> { binding.edittextUser.error = "Digite Login" }
            userPassword.isEmpty() -> { binding.edittextSenha.error = "Digite Senha" }

            businessUser.storeUser(userLogin, userPassword) -> {
                startActivity(Intent(this, MainActivityController::class.java))
                Toast.makeText(this, getString(R.string.bem_vindo),
                    Toast.LENGTH_SHORT).show()
                finish()
            }
            else -> {
                Toast.makeText(this, getString(R.string.erro_usuario),
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}
