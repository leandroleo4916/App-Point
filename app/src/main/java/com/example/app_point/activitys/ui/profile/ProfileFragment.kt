package com.example.app_point.activitys.ui.profile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.entity.EmployeeNameAndPhoto
import com.example.app_point.repository.RepositoryPoint
import com.example.app_point.utils.CaptureDateCurrent
import com.example.app_point.utils.ConverterPhoto
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment: AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val businessEmployee: BusinessEmployee by inject()
    private val captureDateCurrent: CaptureDateCurrent by inject()
    private val repositoryPoint: RepositoryPoint by inject()
    private val photo: ConverterPhoto by inject()
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var employee: EmployeeEntity
    private lateinit var binding: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile)

        binding = findViewById(R.id.container_profile)
        profileViewModel = ProfileViewModel(repositoryPoint)
        val intent = intent
        val args = intent.getSerializableExtra(
            ConstantsEmployee.EMPLOYEE.TABLE_NAME) as EmployeeNameAndPhoto

        employee = businessEmployee.consultEmployeeWithId(args.id)!!

        setInfoEmployee()
        searchPointsEmployee()
        listener()
        observer()

    }

    private fun listener() {

        binding.image_back_perfil.setOnClickListener { this.onBackPressed() }
        binding.search.setOnClickListener{ dialogListEmployee() }
        binding.search_date.setOnClickListener{ calendar() }
        binding.next.setOnClickListener{
            setDate(binding.text_date_time.text.toString(), 1)
        }
        binding.back.setOnClickListener{
            setDate(binding.text_date_time.text.toString(), -1)
        }
    }

    private fun setDate(value: String, position: Int){

        val today = captureDateCurrent.captureDateCurrent()

        if (value == "Pontos de hoje" && position == -1){
            val date = addOrRemoveDate(today, position)
            viewModelSelected(employee.nameEmployee, date)
            binding.text_date_time.text = date
            binding.next.setImageResource(R.drawable.ic_next_write)
        }
        else if(value == "Pontos de hoje" && position == 1){
            Toast.makeText(this, "Não pode avançar", Toast.LENGTH_SHORT).show()
        }
        else{
            val date = addOrRemoveDate(binding.text_date_time.text.toString(), position)
            viewModelSelected(employee.nameEmployee, date)
            if (date == today){
                binding.text_date_time.text = "Pontos de hoje"
                binding.next.setImageResource(R.drawable.ic_next_gray)
            }
            else{
                binding.text_date_time.text = date
                binding.next.setImageResource(R.drawable.ic_next_write)
            }

        }
    }

    private fun addOrRemoveDate(date: String, pos: Int): String{
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val divDate = date.split("/")

        val cal = android.icu.util.Calendar.getInstance()
        cal.set(divDate[2].toInt(), divDate[1].toInt() - 1, divDate[0].toInt())
        cal.add(android.icu.util.Calendar.DAY_OF_MONTH, pos)

        return dateFormat.format(cal.time)
    }

    private fun calendar() {
        val date = Calendar.getInstance()
        val local = Locale("pt", "BR")

        val dateTime = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            date.set(Calendar.MONTH, month)
            date.set(Calendar.YEAR, year)
            val dateSelected = SimpleDateFormat("dd/MM/yyyy", local).format(date.time)
            viewModelSelected("", dateSelected)
        }
        let {
            DatePickerDialog(
                it, dateTime, date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun setInfoEmployee(){

        val photoConverter = photo.converterToBitmap(employee.photo)

        binding.run {
            image_photo_employee.setImageBitmap(photoConverter)
            text_cargo_description.text = employee.cargoEmployee
            text_email_description.text = employee.emailEmployee
            text_phone_description.text = employee.phoneEmployee
            text_admission_description.text = employee.admissaoEmployee
            text_birth_description.text = employee.aniversarioEmployee
            if (employee.vacation == 1) {
                text_vacation_description.text = "De ferias"
                ic_vacation_description.setImageResource(R.drawable.ic_trip)
            }
            if (employee.active == 1) {
                text_active_description.text = "Desativado"
                ic_active_description.setImageResource(R.drawable.ic_disable)
            }
        }
        setProgressHours()
    }

    private fun setProgressHours(){
        val hoursMake = 120
        var pStatus1 = 0
        val hoursExtra = 22
        var pStatus2 = 0

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (pStatus1 <= hoursMake) {
                    withContext(Dispatchers.Main) {
                        binding.progress_total_funcionarios.progress = pStatus1
                        binding.edit_hrs_feitas.text = pStatus1.toString()
                    }
                    delay(20)
                    pStatus1++
                }
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (pStatus2 <= hoursExtra) {
                    withContext(Dispatchers.Main) {
                        binding.progress_funcionarios_ativos.progress = pStatus2
                        binding.text_funcionarios_ativos.text = pStatus2.toString()
                    }
                    delay(20)
                    pStatus2++
                }
            }
        }
    }

    private fun viewModelSelected (name: String, date: String){

        binding.progress_points.visibility = View.VISIBLE
        profileViewModel.getFullPoints(name, date)
    }

    private fun searchPointsEmployee(){

        profileViewModel.getFullPoints(employee.nameEmployee, captureDateCurrent.captureHoraCurrent())
        binding.progress_points.visibility = View.GONE
    }

    private fun observer() {

        profileViewModel.pointsList.observe(this, {
            binding.run {
                text_hora1.text = it?.hora1 ?: "--:--"
                text_hora2.text = it?.hora2 ?: "--:--"
                text_hora3.text = it?.hora3 ?: "--:--"
                text_hora4.text = it?.hora4 ?: "--:--"
            }
            binding.progress_points.visibility = View.GONE
        })
    }

    private fun dialogListEmployee() {

        val inflater = layoutInflater
        val inflateView = inflater.inflate(R.layout.dialog_list_employee, null)

        val list = businessEmployee.consultEmployee()
        val listSpinner= inflateView.findViewById(R.id.spinner_employee) as Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        listSpinner.adapter = adapter
        listSpinner.onItemSelectedListener = this

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.selecione_funcionario))
        alertDialog.setView(inflateView)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Ok") { _, _ ->

            when (val itemSpinner = listSpinner.selectedItem) {
                null -> { }
                else -> {
                    val employee = businessEmployee.consultEmployeeByName(itemSpinner.toString())
                    //resumeFragment(employee)
                }
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
        Snackbar.make(binding.container_profile,
            message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(Color.BLACK)
            .setAction("Ok") {}
            .show()
    }

}