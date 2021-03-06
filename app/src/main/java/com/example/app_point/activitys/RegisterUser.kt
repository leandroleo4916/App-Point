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
import kotlinx.android.synthetic.main.register_user.*
import kotlinx.android.synthetic.main.register_user.edittext_email
import kotlinx.android.synthetic.main.register_user.edittext_username
import kotlinx.android.synthetic.main.register_user.image_back

class RegisterUser : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBusinessUser: BusinessUser
    private lateinit var mSecurityPreferences: SecurityPreferences
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_user)

        auth = FirebaseAuth.getInstance()
        mBusinessUser = BusinessUser(this)
        mSecurityPreferences = SecurityPreferences(this)
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
            name == ""  -> editTextName.error = "Digite Nome"
            email == "" -> editTextEmail.error = "Digite Email"
            senha == "" -> editTextSenha.error == "Digite Senha"
            confirmeSenha == "" -> editTextConfirma.error == "Confirme Senha"
            senha != confirmeSenha -> Toast.makeText(this, getString(R.string.senhas_diferentes), Toast.LENGTH_SHORT).show()
            else -> createEmailAndPassword(name, email, senha)
        }
    }

    private fun createEmailAndPassword(name: String, email: String, password: String){
        auth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful){
                Toast.makeText(this, getString(R.string.cadastro_feito), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                mSecurityPreferences.storeString(ConstantsUser.USER.COLUNAS.NAME, name)
                mSecurityPreferences.storeString(ConstantsUser.USER.COLUNAS.EMAIL, email)
                mSecurityPreferences.storeString(ConstantsUser.USER.COLUNAS.PASSWORD, password)
                finish()

            }else{
                Toast.makeText(this, getString(R.string.nao_foi_possivel_cadastrar), Toast.LENGTH_SHORT).show()
            }
        }
    }
}