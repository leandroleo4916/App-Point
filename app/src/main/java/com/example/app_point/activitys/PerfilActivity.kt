package com.example.app_point.activitys

import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.utils.ConverterPhoto
import com.example.app_point.utils.EmployeeEntity
import kotlinx.android.synthetic.main.activity_perfil.*
import java.io.ByteArrayInputStream

class PerfilActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private val mBusinessEmployee: BusinessEmployee = BusinessEmployee(this)
    private val mListEmployee: BusinessEmployee = BusinessEmployee(this)
    private val mConverterPhoto: ConverterPhoto = ConverterPhoto()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val listEmployee = mBusinessEmployee.consultEmployee()
        buscarEmployee(listEmployee[0])
        listener()
    }

    private fun listener(){
        image_back_perfil.setOnClickListener(this)
        edit_employee.setOnClickListener(this)
        search.setOnClickListener(this)
    }

    private fun buscarEmployee(nomeEmploye: String){
        val position = nomeEmploye
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
            search -> dialogListEmployee()
        }
    }

    private fun dialogListEmployee(){
        // Infla o layout
        val inflater = layoutInflater
        val inflate_view = inflater.inflate(R.layout.dialog_list_employee, null)

        // Capturando Lista de Funcionarios e adiciona ao spinner
        val list = mListEmployee.consultEmployee()
        val listSpinner= inflate_view.findViewById(R.id.spinner_employee) as Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        listSpinner.adapter = adapter
        listSpinner.onItemSelectedListener = this

        // Cria o Dialog
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Selecione o Funcionário")
        alertDialog.setView(inflate_view)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Ok") { dialog, which ->

            // Captura item do Spinner
            val itemSpinner = listSpinner.selectedItem.toString()
            buscarEmployee(itemSpinner)

        }
        alertDialog.setNegativeButton("Cancelar") {
                dialog, which -> Toast.makeText(this, "Cancelado!", Toast.LENGTH_SHORT).show()
        }
        val dialog = alertDialog.create()
        dialog.show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent?.id) {
            R.id.spinner_employee -> {
                parent.getItemAtPosition(position).toString()
            }
        }
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}