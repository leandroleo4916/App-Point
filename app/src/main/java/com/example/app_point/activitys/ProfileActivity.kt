package com.example.app_point.activitys

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.business.CalculationHours
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.databinding.ActivityPerfilBinding
import com.example.app_point.entity.PointsHours
import com.example.app_point.model.AdapterPoints
import com.example.app_point.model.ViewModelPoints
import com.example.app_point.utils.ConverterPhoto
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_perfil.*
import kotlinx.android.synthetic.main.activity_perfil.edit_employee
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val mViewModelPoints by viewModel<ViewModelPoints>()
    private lateinit var mAdapterPoints: AdapterPoints
    private val mBusinessEmployee: BusinessEmployee by inject()
    private lateinit var mPhoto: ConverterPhoto
    private val handler: Handler = Handler()
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var calculationHours: CalculationHours
    private val binding by lazy { ActivityPerfilBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        constraintLayout = findViewById(R.id.container_perfil)
        mPhoto = ConverterPhoto()
        calculationHours = CalculationHours()

        val recycler = findViewById<RecyclerView>(R.id.recyclerViewProfile)
        recycler.layoutManager = LinearLayoutManager(this)
        mAdapterPoints = AdapterPoints(application)
        recycler.adapter = mAdapterPoints

        searchEmployee()
        listener()
        observer()
    }

    private fun searchEmployee() {
        val listEmployee = mBusinessEmployee.consultEmployee()

        if (listEmployee.isNotEmpty()) {
            searchByNameEmployee(listEmployee[0])
            viewModel(listEmployee[0])
        }
        else {
            progress_points.visibility = View.GONE
            showSnackBar(R.string.precisa_add_funcionarios)
        }
    }

    private fun listener(){
        image_back_perfil.setOnClickListener{ finish() }
        edit_employee.setOnClickListener{ editEmployee(text_name_employee.text.toString()) }
        search.setOnClickListener{ dialogListEmployee() }
        float_bottom_perfil.setOnClickListener{
            startActivity(Intent(this, RegisterEmployeeActivity::class.java))
            finish()
        }
        text_name_employee.setOnClickListener{ editEmployee(text_name_employee.text.toString()) }
        search_date.setOnClickListener{ calendar() }
        image_photo_employee.setOnClickListener{ editEmployee(text_name_employee.text.toString()) }
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
            this, dateTime, date.get(Calendar.YEAR), date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun searchByNameEmployee(name: String){
        val dataEmployee = mBusinessEmployee.consultDataEmployee(name)
        text_name_employee.text = dataEmployee!!.nameEmployee
        text_cargo_employee.text = dataEmployee.cargoEmployee
        val photo = dataEmployee.photo
        val photoConverter = mPhoto.converterToBitmap(photo)
        image_photo_employee.setImageBitmap(photoConverter)

        val points = mViewModelPoints.getFullPointsByName(name)

        val hoursMake = 0
        var pStatus1 = 0
        val hoursExtra = 0
        var pStatus2 = 0

        CoroutineScope(Main).launch {
            withContext(Dispatchers.Default) {
                while (pStatus1 <= hoursMake) {
                    handler.post {
                        image_toolbar_hrs.progress = pStatus1
                        edit_hrs_feitas.text = pStatus1.toString()
                    }
                    try {
                        Thread.sleep(20)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    pStatus1++
                }
            }
        }

        CoroutineScope(Main).launch {
            withContext(Dispatchers.Default) {
                while (pStatus2 <= hoursExtra) {
                    handler.post {
                        image_toolbar_.progress = pStatus2
                        edit_hrs_extras.text = pStatus2.toString()
                    }
                    try {
                        Thread.sleep(20)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    pStatus2++
                }
            }
        }
    }

    private fun viewModelSelected(name: String, date: String){
        progress_points.visibility = View.VISIBLE
        mViewModelPoints.getFullEmployee(name, date)
    }

    private fun viewModel(name: String){
        mViewModelPoints.getFullEmployee(name, "")
        text_date_selected.text = getString(R.string.todos)
        progress_points.visibility = View.GONE
    }

    private fun observer(){
        mViewModelPoints.employeeFullList.observe(this, {
            when (it.size) {
                0 -> {
                    showSnackBar(R.string.nenhum_ponto_registrado)
                    progress_points.visibility = View.GONE
                }
                else -> {
                    mAdapterPoints.updateFullEmployee(it)
                    progress_points.visibility = View.GONE
                }
            }
        })
    }

    private fun editEmployee(employee: String){
        if(employee == "Fulano Beltrano"){
            showSnackBar(R.string.precisa_add_funcionarios)
        }else {
            val id = mBusinessEmployee.consultIdEmployee(employee)
            val intent = Intent(this, RegisterEmployeeActivity::class.java)

            intent.putExtra(ConstantsEmployee.EMPLOYEE.COLUMNS.ID, id)
            startActivity(intent)
            finish()
        }
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
            when (val itemSpinner = listSpinner.selectedItem) {
                null -> showSnackBar(R.string.precisa_add_funcionarios)
                else -> {
                    searchByNameEmployee(itemSpinner.toString())
                    viewModel(itemSpinner.toString()) }
            }
        }
        alertDialog.setNegativeButton(R.string.cancelar) { _, _ ->
            showSnackBar(R.string.cancelado)
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

    private fun showSnackBar(message: Int) {
        Snackbar.make(constraintLayout,
            message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(Color.BLACK)
            .setAction("Ok") {}
            .show()
    }
}