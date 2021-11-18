package com.example.app_point.activitys.ui.profile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_point.R
import com.example.app_point.adapters.AdapterPoints
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.interfaces.ItemEmployee
import com.example.app_point.repository.RepositoryPoint
import com.example.app_point.utils.ConverterPhoto
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val adapterPoints: AdapterPoints by inject()
    private val businessEmployee: BusinessEmployee by inject()
    private val repositoryPoint: RepositoryPoint by inject()
    private val photo: ConverterPhoto by inject()
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var listener: ItemEmployee
    private lateinit var binding: View

    companion object { fun newInstance() = ProfileFragment() }

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View {

        binding = inflater.inflate(R.layout.fragment_profile, container, false)
        profileViewModel = ProfileViewModel(repositoryPoint)

        if (arguments == null){
            recycler()
            searchEmployee()
            listener()
            observer()
        }
        else {
            val args = arguments?.let { it.getSerializable("employee") as EmployeeEntity }
            recycler()
            setInfoEmployee(args!!)
            searchPointsEmployee(args.nameEmployee)
            listener()
            observer()
        }

        return binding
    }

    private fun recycler() {
        val recycler = binding.recyclerViewProfile
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapterPoints
    }

    private fun searchEmployee() {

        val listEmployee = businessEmployee.consultEmployee()

        if (listEmployee.isNotEmpty()) {
            searchByNameEmployee(listEmployee[0])
            searchPointsEmployee(listEmployee[0])
        }
        else { binding.progress_points.visibility = View.GONE }
    }

    private fun listener() {

        binding.image_back_perfil.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.edit_employee.setOnClickListener{
            editEmployee(binding.text_name_employee.text.toString())
        }
        binding.search.setOnClickListener{
            dialogListEmployee()
        }
        binding.text_name_employee.setOnClickListener{
            editEmployee(binding.text_name_employee.text.toString())
        }
        binding.search_date.setOnClickListener{
            calendar()
        }
        binding.image_photo_employee.setOnClickListener{
            editEmployee(binding.text_name_employee.text.toString())
        }
    }

    private fun calendar() {
        val nameEmployee = binding.text_name_employee.text.toString()
        val date = Calendar.getInstance()
        val local = Locale("pt", "BR")

        val dateTime = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            date.set(Calendar.MONTH, month)
            date.set(Calendar.YEAR, year)
            val dateSelected = SimpleDateFormat("dd/MM/yyyy", local).format(date.time)
            viewModelSelected(nameEmployee, dateSelected)
            binding.text_date_selected.text = dateSelected
        }
        context?.let {
            DatePickerDialog(
                it, dateTime, date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun setInfoEmployee (args: EmployeeEntity){

        val photoConverter = photo.converterToBitmap(args.photo)

        binding.run {
            text_name_employee.text = args.nameEmployee
            text_cargo_employee.text = args.cargoEmployee
            image_photo_employee.setImageBitmap(photoConverter)
        }
        setProgressHours(binding)
    }

    private fun searchByNameEmployee(name: String){

        val dataEmployee = businessEmployee.consultEmployeeByName(name)
        val image = dataEmployee.photo
        val photoConverter = photo.converterToBitmap(image)

        binding.run {
            text_name_employee.text = dataEmployee.nameEmployee
            text_cargo_employee.text = dataEmployee.cargoEmployee
            image_photo_employee.setImageBitmap(photoConverter)
        }
        setProgressHours(binding)
    }

    private fun setProgressHours(binding: View){
        val hoursMake = 120
        var pStatus1 = 0
        val hoursExtra = 22
        var pStatus2 = 0

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (pStatus1 <= hoursMake) {
                    withContext(Dispatchers.Main) {
                        binding.image_toolbar_hrs.progress = pStatus1
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
                        binding.image_toolbar_.progress = pStatus2
                        binding.edit_hrs_extras.text = pStatus2.toString()
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

    private fun searchPointsEmployee (name: String?){

        if (name != null) {
            profileViewModel.getFullPoints(name, "")
        }
        binding.text_date_selected.text = getString(R.string.todos)
        binding.progress_points.visibility = View.GONE
    }

    private fun observer() {

        profileViewModel.pointsList.observe(viewLifecycleOwner, {
            when (it.size) {
                0 -> {
                    //showSnackBar(R.string.nenhum_ponto_registrado)
                    adapterPoints.updateFullEmployee(it)
                    binding.progress_points.visibility = View.GONE
                }
                else -> {
                    adapterPoints.updateFullEmployee(it)
                    binding.progress_points.visibility = View.GONE
                }
            }
        })
    }

    private fun editEmployee(employee: String){
        if (employee == "Fulano Beltrano") {
            showSnackBar(R.string.precisa_add_funcionarios)
        }
    }

    private fun dialogListEmployee() {

        val inflater = layoutInflater
        val inflateView = inflater.inflate(R.layout.dialog_list_employee, null)

        val list = businessEmployee.consultEmployee()
        val listSpinner= inflateView.findViewById(R.id.spinner_employee) as Spinner
        val adapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, list) }
        listSpinner.adapter = adapter
        listSpinner.onItemSelectedListener = this

        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle(getString(R.string.selecione_funcionario))
        alertDialog.setView(inflateView)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Ok") { _, _ ->

            when (val itemSpinner = listSpinner.selectedItem) {
                null -> {}
                else -> {
                    val employee = businessEmployee.consultEmployeeByName(itemSpinner.toString())
                    resumeFragment(employee)
                }
            }
        }
        alertDialog.setNegativeButton(R.string.cancelar) { _, _ ->
            showSnackBar(R.string.cancelado)
        }
        val dialog = alertDialog.create()
        dialog.show()
    }

    private fun resumeFragment(employee: EmployeeEntity) {
        if (context is ItemEmployee) { listener = context as ItemEmployee
        } else { throw ClassCastException("$context must implemented") }
        listener.openFragmentProfile(employee)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent?.id) {
            R.id.spinner_employee -> { parent.getItemAtPosition(position).toString() }
        }
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun showSnackBar(message: Int) {
        Snackbar.make(binding.container_perfil,
            message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(Color.BLACK)
            .setAction("Ok") {}
            .show()
    }

}