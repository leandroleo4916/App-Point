package com.example.app_point.activitys.ui.profile

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.entity.EmployeeEntityFull
import com.example.app_point.interfaces.IVisibilityNavView
import com.example.app_point.interfaces.ItemEmployee
import com.example.app_point.repository.RepositoryPoint
import com.example.app_point.utils.CalculateHours
import com.example.app_point.utils.CaptureDateCurrent
import com.example.app_point.utils.ConverterPhoto
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment: Fragment(), AdapterView.OnItemSelectedListener {

    private val businessEmployee: BusinessEmployee by inject()
    private val captureDateCurrent: CaptureDateCurrent by inject()
    private val repositoryPoint: RepositoryPoint by inject()
    private val photo: ConverterPhoto by inject()
    private val hours: CalculateHours by inject()
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var employee: EmployeeEntityFull
    private lateinit var binding: View
    private lateinit var openRegister: ItemEmployee
    private lateinit var closeFragment: IVisibilityNavView
    private val today = captureDateCurrent.captureDateCurrent()

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View {

        binding = inflater.inflate(R.layout.fragment_profile, container, false)
        profileViewModel = ProfileViewModel(repositoryPoint)
        val args = arguments?.let { it.getSerializable("id") as Int }
        employee = args?.let { businessEmployee.consultEmployeeWithId(it) }!!

        setInfoEmployee()
        searchPointsEmployee()
        listener()
        observer()

        return binding
    }

    private fun listener() {

        binding.image_back_perfil.setOnClickListener { activity?.onBackPressed() }
        binding.option_profile.setOnClickListener{ optionMenuProfile() }
        binding.search_date.setOnClickListener{ calendar() }
        binding.next.setOnClickListener{
            setDate(binding.text_date_time.text.toString(), 1)
        }
        binding.back.setOnClickListener{
            setDate(binding.text_date_time.text.toString(), -1)
        }
    }

    private fun setDate(value: String, position: Int){

        if (value == "Pontos de hoje" && position == -1){
            val date = addOrRemoveDate(today, position)
            viewModelSelected(employee.id, date)
            binding.text_date_time.text = date
            binding.next.setImageResource(R.drawable.ic_next_write)
        }
        else if (value == "Pontos de hoje" && position == 1){
            showSnackBar("Não pode selecionar datas futuras!")
        }
        else{
            val date = addOrRemoveDate(binding.text_date_time.text.toString(), position)
            viewModelSelected(employee.id, date)
            
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

            when {
                dateSelected == today -> {
                    binding.text_date_time.text = "Pontos de hoje"
                    binding.next.setImageResource(R.drawable.ic_next_gray)
                    viewModelSelected(employee.id, dateSelected)
                }
                compareDate(dateSelected, captureDateCurrent.captureDateCurrent()) -> {
                    showSnackBar("Não pode selecionar datas futuras!")
                }
                else -> {
                    viewModelSelected(employee.id, dateSelected)
                    binding.text_date_time.text = dateSelected
                    binding.next.setImageResource(R.drawable.ic_next_write)
                }
            }
        }
        let {
            context?.let { it1 ->
                DatePickerDialog(
                    it1, dateTime, date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
    }

    private fun compareDate(dateSelected: String, dateString: String): Boolean{

        val format = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val dateCurrent = captureDateCurrent.captureDateCurrent()
        val date1 = format.parse(dateSelected)

        val date2 =
            if (dateString == "Pontos de hoje") format.parse(dateCurrent)
            else format.parse(dateCurrent)
        return date1!!.after(date2)
    }

    private fun setInfoEmployee(){

        val photoConverter = photo.converterToBitmap(employee.photo)

        binding.run {
            image_photo_employee.setImageBitmap(photoConverter)
            text_title_name_employee.text = employee.nameEmployee
            text_cargo_description.text = employee.cargoEmployee
            text_email_description.text = employee.emailEmployee
            text_phone_description.text = employee.phoneEmployee
            text_admission_description.text = employee.admissaoEmployee
            text_birth_description.text = employee.aniversarioEmployee

            text_vacation_description.text = employee.status
            when (employee.status) {
                "Trabalhando" -> {
                    ic_vacation_description.setImageResource(R.drawable.ic_working)
                }
                "De férias" -> {
                    ic_vacation_description.setImageResource(R.drawable.ic_trip)
                }
                else -> {
                    ic_vacation_description.setImageResource(R.drawable.ic_disable)
                }
            }

            text_time1_description.text = hours.convertMinutesInHoursString(employee.horario1)
            text_time2_description.text = hours.convertMinutesInHoursString(employee.horario2)
            text_time3_description.text = hours.convertMinutesInHoursString(employee.horario3)
            text_time4_description.text = hours.convertMinutesInHoursString(employee.horario4)
        }

        setProgressHours()
    }

    private fun setProgressHours(){
        val hoursMake = 20
        var pStatus1 = 0
        val hoursExtra = 22
        var pStatus2 = 0

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                while (pStatus1 <= hoursMake) {
                    withContext(Dispatchers.Main) {
                        binding.progress_hours_done_cyrcle.progress = pStatus1
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
                        binding.progress_funcionarios_desativados.progress = pStatus2
                        binding.text_funcionarios_desativados.text = pStatus2.toString()
                    }
                    delay(20)
                    pStatus2++
                }
            }
        }
    }

    private fun viewModelSelected(id: Int, date: String){
        binding.progress_points.visibility = View.VISIBLE
        profileViewModel.getFullPoints(id, date)
    }

    private fun searchPointsEmployee(){
        profileViewModel.getFullPoints(employee.id, captureDateCurrent.captureDateCurrent())
        binding.progress_points.visibility = View.GONE
    }

    private fun observer() {

        profileViewModel.pointsList.observe(viewLifecycleOwner, {
            binding.run {
                text_hora1.text = it?.horario1 ?: "--:--"
                text_hora2.text = it?.horario2 ?: "--:--"
                text_hora3.text = it?.horario3 ?: "--:--"
                text_hora4.text = it?.horario4 ?: "--:--"
            }
            binding.progress_points.visibility = View.GONE
        })
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent?.id) {
            R.id.spinner_employee -> {
                parent.getItemAtPosition(position).toString()
            }
        }
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun optionMenuProfile(){

        val popMenu = PopupMenu(context, binding.option_profile)

        val value = when (employee.status) {
            "Trabalhando" -> {
                popMenu.menuInflater.inflate(R.menu.menu_profile, popMenu.menu)
                "De férias"
            }
            "De férias" -> {
                popMenu.menuInflater.inflate(R.menu.menu_profile_work, popMenu.menu)
                "Trabalhando"
            }
            else -> {
                popMenu.menuInflater.inflate(R.menu.menu_profile_active, popMenu.menu)
                "Trabalhando"
            }
        }
        val disable = if (employee.status == "Trabalhando"){
            "Desativado"
        }else if (employee.status == "De férias"){
            "Desativado"
        }else {
            "Trabalhando"
        }
        popMenu.setOnMenuItemClickListener { item ->
            when (item!!.itemId) {
                R.id.edit_profile -> {
                    if (context is ItemEmployee){
                        openRegister = context as ItemEmployee
                        openRegister.openFragmentRegister(employee.id)
                    }
                }
                R.id.ferias_profile -> {
                    val status = profileViewModel.modifyStatusEmployee(employee, value)
                    showSnackBar(status)
                    employee = businessEmployee.consultEmployeeWithId(employee.id)!!
                    setInfoEmployee()
                }
                R.id.desativar_profile -> {
                    val status = profileViewModel.modifyStatusEmployee(employee, disable)
                    showSnackBar(status)
                    employee = businessEmployee.consultEmployeeWithId(employee.id)!!
                    setInfoEmployee()
                }
            }
            true
        }
        popMenu.show()
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.container_profile,
            message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(Color.BLACK)
            .setAction("Ok") {}
            .show()
    }

}