package com.example.app_point.fragments.register

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.model.EmployeeAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RegisterEmployeeFragment : Fragment() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var mBusinessEmployee: BusinessEmployee
    private val mAdapterEmployee: EmployeeAdapter = EmployeeAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val float = root.findViewById(R.id.float_button_employee) as FloatingActionButton
        float.setOnClickListener { showDialog() }

        // Cria a recycler
        val recycler = root.findViewById<RecyclerView>(R.id.recyclerEmployee)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = mAdapterEmployee

        mBusinessEmployee = BusinessEmployee(context)
        registerViewModel.getEmployeeList()
        registerViewModel.getOfficeList()

        observer()
        return root
    }

    private fun observer() {
        registerViewModel.textEmployee.observe(viewLifecycleOwner, Observer {
            mAdapterEmployee.updateEmployee(it)
        })
        registerViewModel.textOffice.observe(viewLifecycleOwner, Observer {
            mAdapterEmployee.updateOffice(it)
        })
    }

    private fun showDialog() {
        val inflater = layoutInflater
        val inflate_view = inflater.inflate(R.layout.dialog_employee, null)

        val employee = inflate_view.findViewById(R.id.edit_text_employee) as EditText
        val office = inflate_view.findViewById(R.id.edit_text_office) as EditText
        val email = inflate_view.findViewById(R.id.edit_text_email) as EditText
        val phone = inflate_view.findViewById(R.id.edit_text_telefone) as EditText
        val admission = inflate_view.findViewById(R.id.edit_text_admission) as EditText
        val hour1 = inflate_view.findViewById(R.id.edit_text_hour_1) as EditText
        val hour2 = inflate_view.findViewById(R.id.edit_text_hour_2) as EditText
        val hour3 = inflate_view.findViewById(R.id.edit_text_hour_3) as EditText
        val hour4 = inflate_view.findViewById(R.id.edit_text_hour_4) as EditText

        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Registrar Funcionário")
        alertDialog.setView(inflate_view)
        alertDialog.setCancelable(false)

        alertDialog.setNegativeButton("Cancelar") { dialog, which ->
            Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show()
        }
        alertDialog.setPositiveButton("Salvar") { dialog, which ->

            val mEmployee = employee.text.toString()
            val mOffice = office.text.toString()
            val mEmail = email.text.toString()
            val mPhone = phone.text.toString()
            val mAdmission = admission.text.toString()
            val mHour1 = hour1.text.toString()
            val mHour2 = hour2.text.toString()
            val mHour3 = hour3.text.toString()
            val mHour4 = hour4.text.toString()

            when {
                mEmployee == "" || mEmail == "" || mOffice == "" || mPhone == "" || mAdmission == ""
                        || mHour1 == "" || mHour2 == "" || mHour3 == "" || mHour4 == "" -> {
                        Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                }
                mBusinessEmployee.getEmployee(
                    mEmployee, mOffice, mEmail, mPhone, mAdmission, mHour1, mHour2, mHour3, mHour4) -> {
                    Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    registerViewModel.getEmployeeList()
                    registerViewModel.getOfficeList()
                }
                else -> {
                    Toast.makeText(context, "Algo deu errado, tente novamente!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val dialog = alertDialog.create()
        dialog.show()
    }
}