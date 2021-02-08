package com.example.app_point.activitys

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.utils.ConverterPhoto
import com.example.app_point.utils.EmployeeEntity
import kotlinx.android.synthetic.main.activity_perfil.*
import java.io.ByteArrayInputStream

class PerfilActivity : AppCompatActivity(), View.OnClickListener {

    private val mBusinessEmployee: BusinessEmployee = BusinessEmployee(this)
    private val mConverterPhoto: ConverterPhoto = ConverterPhoto()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val listEmployee = mBusinessEmployee.consultEmployee()
        buscarEmployee(listEmployee)
        listener()
    }

    private fun listener(){
        image_back_perfil.setOnClickListener(this)
        edit_employee.setOnClickListener(this)
    }

    private fun buscarEmployee(nomeEmploye: List<String>){
        val position = nomeEmploye[0]
        val dadosEmployee: EmployeeEntity = mBusinessEmployee.consultDadosEmployee(position)!!
        val image = mConverterPhoto.converterToBitmap(dadosEmployee.photo!!)

        image_photo_employee.setImageBitmap(image)
        text_name_employee.setText(dadosEmployee.nameEmployee)
        text_cargo_employee.setText(dadosEmployee.cargoEmployee)
        text_toolbar_email.setText(dadosEmployee.emailEmployee)
        text_toolbar_phone.setText(dadosEmployee.phoneEmployee)
        text_toolbar_birthday.setText(dadosEmployee.aniversarioEmployee)
        text_toolbar_admissao.setText(dadosEmployee.admissaoEmployee)
        text_toolbar_hora1.setText(dadosEmployee.horario1)
        text_toolbar_hora2.setText(dadosEmployee.horario2)
        text_toolbar_hora3.setText(dadosEmployee.horario3)
        text_toolbar_hora4.setText(dadosEmployee.horario4)
    }

    override fun onClick(view: View?) {
        when(view){
            image_back_perfil -> finish()
            edit_employee -> startActivity(Intent(this, RegisterEmployeeActivity::class.java))
        }
    }

}