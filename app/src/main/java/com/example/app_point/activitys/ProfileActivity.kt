package com.example.app_point.activitys

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.business.CalculationHours
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.databinding.ActivityPerfilBinding
import com.example.app_point.model.AdapterPoints
import com.example.app_point.model.ViewModelPoints
import com.example.app_point.utils.ConverterPhoto
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val mViewModelPoints by viewModel<ViewModelPoints>()
    private val mAdapterPoints: AdapterPoints by inject()
    private val mBusinessEmployee: BusinessEmployee by inject()
    private lateinit var mPhoto: ConverterPhoto
    private val handler: Handler = Handler()
    private lateinit var constraintLayout: ConstraintLayout
    private val calculationHours: CalculationHours by inject()
    private val binding by lazy { ActivityPerfilBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        constraintLayout = findViewById(R.id.container_perfil)
        mPhoto = ConverterPhoto()

        val recycler = findViewById<RecyclerView>(R.id.recyclerViewProfile)
        recycler.layoutManager = LinearLayoutManager(this)
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
            binding.progressPoints.visibility = View.GONE
            showSnackBar(R.string.precisa_add_funcionarios)
        }
    }

    private fun listener(){
        binding.imageBackPerfil.setOnClickListener{ finish() }
        binding.editEmployee.setOnClickListener{
            editEmployee(binding.textNameEmployee.text.toString())
        }
        binding.search.setOnClickListener{ dialogListEmployee() }
        binding.floatBottomPerfil.setOnClickListener{
            startActivity(Intent(this, RegisterEmployeeActivity::class.java))
            finish()
        }
        binding.textNameEmployee.setOnClickListener{
            editEmployee(binding.textNameEmployee.text.toString())
        }
        binding.searchDate.setOnClickListener{ calendar() }
        binding.imagePhotoEmployee.setOnClickListener{
            editEmployee(binding.textNameEmployee.text.toString())
        }
    }

    private fun calendar(){
        val nameEmployee = binding.textNameEmployee.text.toString()
        val date = Calendar.getInstance()
        val local = Locale("pt", "BR")

        val dateTime = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            date.set(Calendar.MONTH, month)
            date.set(Calendar.YEAR, year)
            val dateSelected = SimpleDateFormat("dd/mm/yyyy", local).format(date.time)
            viewModelSelected(nameEmployee, dateSelected)
            binding.textDateSelected.text = dateSelected
        }
        DatePickerDialog(
            this, dateTime, date.get(Calendar.YEAR), date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun searchByNameEmployee(name: String){
        val dataEmployee = mBusinessEmployee.consultDataEmployee(name)
        binding.textNameEmployee.text = dataEmployee!!.nameEmployee
        binding.textCargoEmployee.text = dataEmployee.cargoEmployee
        val photo = dataEmployee.photo
        val photoConverter = mPhoto.converterToBitmap(photo)
        binding.imagePhotoEmployee.setImageBitmap(photoConverter)

        val points = mViewModelPoints.getFullPointsByName(name)

        val hoursMake = 120
        var pStatus1 = 0
        val hoursExtra = 22
        var pStatus2 = 0

        CoroutineScope(Main).launch {
            withContext(Dispatchers.Default) {
                while (pStatus1 <= hoursMake) {
                    handler.post {
                        binding.imageToolbarHrs.progress = pStatus1
                        binding.editHrsFeitas.text = pStatus1.toString()
                    }
                    delay(20)
                    pStatus1++
                }
            }
        }

        CoroutineScope(Main).launch {
            withContext(Dispatchers.Default) {
                while (pStatus2 <= hoursExtra) {
                    handler.post {
                        binding.imageToolbar.progress = pStatus2
                        binding.editHrsExtras.text = pStatus2.toString()
                    }
                    delay(20)
                    pStatus2++
                }
            }
        }
    }

    private fun viewModelSelected(name: String, date: String){
        binding.progressPoints.visibility = View.VISIBLE
        mViewModelPoints.getFullEmployee(name, date)
    }

    private fun viewModel(name: String){
        mViewModelPoints.getFullEmployee(name, "")
        binding.textDateSelected.text = getString(R.string.todos)
        binding.progressPoints.visibility = View.GONE
    }

    private fun observer(){
        mViewModelPoints.employeeFullList.observe(this, {
            when (it.size) {
                0 -> {
                    showSnackBar(R.string.nenhum_ponto_registrado)
                    mAdapterPoints.updateFullEmployee(it)
                    binding.progressPoints.visibility = View.GONE
                }
                else -> {
                    mAdapterPoints.updateFullEmployee(it)
                    binding.progressPoints.visibility = View.GONE
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
        Snackbar.make(binding.containerPerfil,
            message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(Color.BLACK)
            .setAction("Ok") {}
            .show()
    }
}