package com.example.app_point.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.app_point.R
import kotlinx.android.synthetic.main.activity_employee.*

class EmployeeActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)

        listener()

    }

    private fun listener(){
        image_in_register.setOnClickListener(this)
        image_in_perfil.setOnClickListener(this)
        image_in_clock.setOnClickListener(this)
        image_in_opcoes.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            image_in_register -> {startActivity(Intent(this, RegisterEmployeeActivity::class.java))}
            image_in_perfil -> {startActivity(Intent(this, PerfilActivity::class.java))}
            image_in_clock -> {startActivity(Intent(this, PontosActivity::class.java))}
            image_in_opcoes -> {startActivity(Intent(this, ToolsActivity::class.java))}
        }
    }
}