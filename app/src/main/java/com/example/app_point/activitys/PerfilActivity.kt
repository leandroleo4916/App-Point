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
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.utils.ConverterPhoto
import com.example.app_point.entity.EmployeeEntity
import kotlinx.android.synthetic.main.activity_perfil.*

class PerfilActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var mBusinessEmployee: BusinessEmployee
    private val mConverterPhoto: ConverterPhoto = ConverterPhoto()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Captura do DB uma lista de Funcionários e mostra o que está na primeira posição.
        mBusinessEmployee = BusinessEmployee(this)
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

    // Captura id do funcionário e envia para Activity Register para edição
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
        val inflateView = inflater.inflate(R.layout.dialog_list_employee, null)

        // Capturando Lista de Funcionarios e adiciona ao spinner
        val list = mBusinessEmployee.consultEmployee()
        val listSpinner= inflateView.findViewById(R.id.spinner_employee) as Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        listSpinner.adapter = adapter
        listSpinner.onItemSelectedListener = this

        // Cria o Dialog
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.selecione_funcionario))
        alertDialog.setView(inflateView)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Ok") { _, _ ->

            // Captura item do Spinner
            val itemSpinner = listSpinner.selectedItem.toString()
            buscarEmployee(itemSpinner)

        }
        alertDialog.setNegativeButton("Cancelar") { _, _ ->
            Toast.makeText(this, "Cancelado!", Toast.LENGTH_SHORT).show()
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

    }

}