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
import androidx.lifecycle.ViewModelProvider
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.utils.ConverterPhoto
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.model.PointsAdapter
import com.example.app_point.model.ViewModel
import com.example.app_point.repository.RepositoryPoint
import kotlinx.android.synthetic.main.activity_perfil.*

class ProfileActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var mViewModel: ViewModel
    private lateinit var mPontosAdapter: PointsAdapter
    private lateinit var mBusinessEmployee: BusinessEmployee
    private val mConverterPhoto: ConverterPhoto = ConverterPhoto()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        mViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        mPontosAdapter = PointsAdapter(application)

        // Captures a list employee and shows what is in the first position
        mBusinessEmployee = BusinessEmployee(this)
        val listEmployee = mBusinessEmployee.consultEmployee()
        when {
            listEmployee.isNotEmpty() -> {
                searchEmployee(listEmployee[0])
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

    private fun searchEmployee(nomeEmploye: String){
        val dataEmployee: EmployeeEntity = mBusinessEmployee.consultDadosEmployee(nomeEmploye)!!
        val image = mConverterPhoto.converterToBitmap(dataEmployee.photo)

        image_photo_employee.setImageBitmap(image)
        text_name_employee.text = dataEmployee.nameEmployee
        text_cargo_employee.text = dataEmployee.cargoEmployee
        text_toolbar_email.text = dataEmployee.emailEmployee
        text_toolbar_phone.text = dataEmployee.phoneEmployee
        text_toolbar_birthday.text = dataEmployee.aniversarioEmployee
        text_toolbar_admissao.text = dataEmployee.admissaoEmployee
        text_toolbar_hora1.text = dataEmployee.horario1
        text_toolbar_hora2.text = dataEmployee.horario2
        text_toolbar_hora3.text = dataEmployee.horario3
        text_toolbar_hora4.text = dataEmployee.horario4
    }

    // Captures id employee and send to Activity Register to edition
    private fun editEmployee(employee: String){
        val id: EmployeeEntity = mBusinessEmployee.consultDadosEmployee(employee)!!
        val intent = Intent(this, RegisterEmployeeActivity::class.java)

        intent.putExtra(ConstantsEmployee.EMPLOYEE.COLUMNS.ID, id.id)
        startActivity(intent)
        finish()
    }

    private fun dialogListEmployee(){
        // Inflater the layout
        val inflater = layoutInflater
        val inflateView = inflater.inflate(R.layout.dialog_list_employee, null)

        // Captures a list employee and add to spinner
        val list = mBusinessEmployee.consultEmployee()
        val listSpinner= inflateView.findViewById(R.id.spinner_employee) as Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        listSpinner.adapter = adapter
        listSpinner.onItemSelectedListener = this

        // Create the Dialog
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.selecione_funcionario))
        alertDialog.setView(inflateView)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Ok") { _, _ ->

            // Captures item of Spinner
            val itemSpinner = listSpinner.selectedItem.toString()
            searchEmployee(itemSpinner)

        }
        alertDialog.setNegativeButton(R.string.cancelar) { _, _ ->
            Toast.makeText(this, R.string.cancelado, Toast.LENGTH_SHORT).show()
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