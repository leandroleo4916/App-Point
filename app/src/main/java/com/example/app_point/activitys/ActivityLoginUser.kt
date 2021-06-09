package com.example.app_point.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.app_point.R
import com.example.app_point.business.BusinessUser
import com.example.app_point.constants.ConstantsUser
import com.example.app_point.utils.SecurityPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLoginUser : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBusinessUser: BusinessUser
    private lateinit var mSecurityPreferences: SecurityPreferences
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
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

    // Login automatic
    private fun verifyLoggedUser(){

        val email = mSecurityPreferences.getStoredString(ConstantsUser.USER.COLUNAS.EMAIL)
        val password = mSecurityPreferences.getStoredString(ConstantsUser.USER.COLUNAS.PASSWORD)

        if (email != "" && password != ""){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    // Captures and valida User Admin
    private fun loginUser(){
        val userLogin = edittext_user.text.toString()
        val userPassword = edittext_senha.text.toString()
        val editTextUser = edittext_user
        val editTextPassword = edittext_senha

        when {
            userLogin == "" -> { editTextUser.error = "Digite Login" }
            userPassword == "" -> { editTextPassword.error = "Digite Senha" }
            else -> { signIn(userLogin, userPassword) }
        }
    }

    private fun signIn(email: String, password: String){
        auth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful){
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this, getString(R.string.bem_vindo), Toast.LENGTH_SHORT).show()
                mSecurityPreferences.storeString(ConstantsUser.USER.COLUNAS.NAME, "")
                mSecurityPreferences.storeString(ConstantsUser.USER.COLUNAS.EMAIL, email)
                mSecurityPreferences.storeString(ConstantsUser.USER.COLUNAS.PASSWORD, password)
                finish()
            }else{
                Toast.makeText(this, getString(R.string.erro_usuario), Toast.LENGTH_SHORT).show()
            }
        }
    }

}
