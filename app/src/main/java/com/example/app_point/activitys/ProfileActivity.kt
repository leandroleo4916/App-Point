package com.example.app_point.activitys

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.model.AdapterPoints
import com.example.app_point.model.ViewModelPoints
import com.example.app_point.utils.ConverterPhoto
import kotlinx.android.synthetic.main.activity_perfil.*
import kotlinx.android.synthetic.main.activity_perfil.edit_employee
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var mViewModelPoints: ViewModelPoints
    private lateinit var mAdapterPoints: AdapterPoints
    private lateinit var mBusinessEmployee: BusinessEmployee
    private lateinit var mPhoto: ConverterPhoto
    private val handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        mViewModelPoints = ViewModelProvider(this).get(ViewModelPoints::class.java)
        mPhoto = ConverterPhoto()

        val recycler = findViewById<RecyclerView>(R.id.recyclerViewProfile)
        recycler.layoutManager = LinearLayoutManager(this)
        mAdapterPoints = AdapterPoints(application)
        recycler.adapter = mAdapterPoints

        // Captures a list employee and shows what is in the first position

        buscarFuncionario()
        listener()
        observer()
    }

    private fun buscarFuncionario() {
        mBusinessEmployee = BusinessEmployee(this)
        val listEmployee = mBusinessEmployee.consultEmployee()

        if (listEmployee.isNotEmpty()) {
            searchEmployee(listEmployee[0])
            viewModel(listEmployee[0])
        }
        else {
            Thread{
                // Block Thread
                Thread.sleep(1000)
                runOnUiThread {
                    progress_points.visibility = View.GONE
                    Toast.makeText(this, "Ainda não foi adicionado funcionários", Toast.LENGTH_LONG).show()
                }
            }.start()
        }
    }

    private fun listener(){
        image_back_perfil.setOnClickListener(this)
        edit_employee.setOnClickListener(this)
        search.setOnClickListener(this)
        float_bottom_perfil.setOnClickListener(this)
        text_name_employee.setOnClickListener(this)
        search_date.setOnClickListener(this)
        text_date_selected.setOnClickListener(this)
        image_photo_employee.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            image_back_perfil -> finish()
            edit_employee -> editEmployee(text_name_employee.text.toString())
            image_photo_employee -> editEmployee(text_name_employee.text.toString())
            search -> dialogListEmployee()
            text_name_employee -> editEmployee(text_name_employee.text.toString())
            search_date -> calendar()
            float_bottom_perfil -> {
                startActivity(Intent(this, RegisterEmployeeActivity::class.java))
                finish()
            }
        }
    }

    @SuppressLint("SimpleDateFormat", "WeekBasedYear")
    private fun calendar(){
        val nameEmployee = text_name_employee.text.toString()
        val date = Calendar.getInstance()

        val dateTime = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            date.set(Calendar.MONTH, month)
            date.set(Calendar.YEAR, year)
            val dateSelected = SimpleDateFormat("dd/MM/YYYY").format(date.time)
            viewModelSelected(nameEmployee, dateSelected)
            text_date_selected.text = dateSelected
        }
        DatePickerDialog(
            this, dateTime, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun searchEmployee(nomeEmployee: String){
        val dataEmployee = mBusinessEmployee.consultDadosEmployee(nomeEmployee)
        text_name_employee.text = dataEmployee!!.nameEmployee
        text_cargo_employee.text = dataEmployee.cargoEmployee
        val photo = dataEmployee.photo
        val photoConverter = mPhoto.converterToBitmap(photo)
        image_photo_employee.setImageBitmap(photoConverter)

        Thread {
            val numFicticio1 = 92
            var pStatus = 0

            while (pStatus <= numFicticio1) {
                handler.post {
                    image_toolbar_hrs.progress = pStatus
                    edit_hrs_feitas.text = pStatus.toString()
                }
                try { Thread.sleep(20) }
                catch (e: InterruptedException) { e.printStackTrace() }
                pStatus++
            }
        }.start()

        Thread {
            val numFicticio2 = 23
            var pStatus = 0

            while (pStatus <= numFicticio2) {
                handler.post {
                    image_toolbar_.progress = pStatus
                    edit_hrs_extras.text = pStatus.toString()
                }
                try { Thread.sleep(20) }
                catch (e: InterruptedException) { e.printStackTrace() }
                pStatus++
            }
        }.start()
    }

    private fun viewModelSelected(name: String, date: String){
        progress_points.visibility = View.VISIBLE
        Thread{
            // Block Thread
            Thread.sleep(1000)
            runOnUiThread {
                mViewModelPoints.getFullEmployee(name, date)
                progress_points.visibility = View.GONE
            }
        }.start()
    }

    private fun viewModel(name: String){
        Thread{
            // Block Thread
            Thread.sleep(1000)
            runOnUiThread {
                mViewModelPoints.getFullEmployee(name, "")
                text_date_selected.text = getString(R.string.todos)
                progress_points.visibility = View.GONE
            }
        }.start()
    }

    private fun observer(){
        mViewModelPoints.employeeFullList.observe(this, {
            when (it.size) {
                0 -> { Toast.makeText(this, "Ainda não foi adicionado Pontos", Toast.LENGTH_LONG).show() }
                else -> { mAdapterPoints.updateFullEmployee(it) }
            }
        })
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
            viewModel(itemSpinner)

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
    override fun onNothingSelected(parent: AdapterView<*>?) {}
}