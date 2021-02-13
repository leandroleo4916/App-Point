package com.example.app_point.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.app_point.R
import com.example.app_point.business.BusinessUser
import com.example.app_point.Constants.ConstantsUser
import com.example.app_point.utils.SecurityPreferences
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLoginUser : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBusinessUser: BusinessUser
    private lateinit var mSecurityPreferences: SecurityPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mBusinessUser = BusinessUser(this)
        mSecurityPreferences = SecurityPreferences(this)
        listener()
        verifyLoggedUser()
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

    private fun verifyLoggedUser(){
        val nome = mSecurityPreferences.getStoredString(ConstantsUser.USER.COLUNAS.NAME)
        val email = mSecurityPreferences.getStoredString(ConstantsUser.USER.COLUNAS.EMAIL)
        val password = mSecurityPreferences.getStoredString(ConstantsUser.USER.COLUNAS.PASSWORD)

        if (email != "" && password != ""){

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(ConstantsUser.USER.COLUNAS.NAME, nome)

            startActivity(intent)
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