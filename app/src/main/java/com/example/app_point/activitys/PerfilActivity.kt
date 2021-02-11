package com.example.app_point.activitys

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.database.ConstantsEmployee
import com.example.app_point.utils.ConverterPhoto
import com.example.app_point.utils.EmployeeEntity
import kotlinx.android.synthetic.main.activity_perfil.*

class PerfilActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private val mBusinessEmployee: BusinessEmployee = BusinessEmployee(this)
    private val mListEmployee: BusinessEmployee = BusinessEmployee(this)
    private val mConverterPhoto: ConverterPhoto = ConverterPhoto()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val listEmployee = mBusinessEmployee.consultEmployee()
        when {
            listEmployee.isNotEmpty() -> {
                buscarEmployee(listEmployee[0])
            }
        }
        listener()
    }

    private fun listener(){
        image_back_perfil.setOnClickListener(this)
        edit_employee.setOnClickListener(this)
        search.setOnClickListener(this)
        float_bottom_perfil.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            image_back_perfil -> finish()
            edit_employee -> editEmployee(text_name_employee.text.toString())
            search -> dialogListEmployee()
            float_bottom_perfil -> {
                startActivity(Intent(this, RegisterEmployeeActivity::class.java))
                finish()
            }
        }
    }

    private fun buscarEmployee(nomeEmploye: String){
        val dadosEmployee: EmployeeEntity = mBusinessEmployee.consultDadosEmployee(nomeEmploye)!!
        val image = mConverterPhoto.converterToBitmap(dadosEmployee.photo)

        image_photo_employee.setImageBitmap(image)
        text_name_employee.text = dadosEmployee.nameEmployee
        text_cargo_employee.text = dadosEmployee.cargoEmployee
        text_toolbar_email.text = dadosEmployee.emailEmployee
        text_toolbar_phone.text = dadosEmployee.phoneEmployee
        text_toolbar_birthday.text = dadosEmployee.aniversarioEmployee
        text_toolbar_admissao.text = dadosEmployee.admissaoEmployee
        text_toolbar_hora1.text = dadosEmployee.horario1
        text_toolbar_hora2.text = dadosEmployee.horario2
        text_toolbar_hora3.text = dadosEmployee.horario3
        text_toolbar_hora4.text = dadosEmployee.horario4
    }

    private fun editEmployee(employee: String){

        val id: EmployeeEntity = mBusinessEmployee.consultDadosEmployee(employee)!!
        val intent = Intent(this, RegisterEmployeeActivity::class.java)

        intent.putExtra(ConstantsEmployee.EMPLOYEE.COLUMNS.ID, id.id)
        startActivity(intent)
        finish()

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