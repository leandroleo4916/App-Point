package com.example.app_point.activitys.ui.inicio

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_point.R
import com.example.app_point.adapters.EmployeeAdapterHome
import com.example.app_point.adapters.PointsAdapter
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.business.BusinessPoints
import com.example.app_point.constants.ConstantsUser
import com.example.app_point.database.DataBaseEmployee
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.interfaces.ItemClickEmployeeHome
import com.example.app_point.interfaces.ItemEmployee
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.repository.RepositoryPoint
import com.example.app_point.utils.CaptureDateCurrent
import com.example.app_point.utils.SecurityPreferences
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_employee.*
import kotlinx.android.synthetic.main.fragment_employee.container_employee
import kotlinx.android.synthetic.main.fragment_inicio.*
import kotlinx.android.synthetic.main.fragment_inicio.view.*
import org.koin.android.ext.android.inject
import java.lang.ClassCastException

class InicioFragment : Fragment(), AdapterView.OnItemSelectedListener, ItemClickEmployeeHome {

    private lateinit var listener: ItemEmployee
    private val pointAdapter: PointsAdapter by inject()
    private val listEmployee: BusinessEmployee by inject()
    private val captureDateCurrent: CaptureDateCurrent by inject()
    private val businessPoints: BusinessPoints by inject()
    private val securityPreferences: SecurityPreferences by inject()

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {

        val binding = inflater.inflate(R.layout.fragment_inicio, container, false)
        val dataBase = DataBaseEmployee(context)
        val repositoryPoint = RepositoryPoint(dataBase)
        val repositoryEmployee = RepositoryEmployee(dataBase)
        val homeViewModel = InicioViewModel(repositoryPoint, repositoryEmployee)
        val employeeAdapter = EmployeeAdapterHome(context, this)

        recyclerPoints(binding)
        recyclerEmployee(binding, employeeAdapter)
        searchPointsAndEmployee(binding, homeViewModel)
        salutation(binding)
        observe(homeViewModel, employeeAdapter)
        listener(binding, homeViewModel)

        return binding
    }

    private fun listener(binding: View, homeViewModel: InicioViewModel) {

        binding.image_add_ponto.setOnClickListener {
            dialogPoint(captureDateCurrent, listEmployee, binding, homeViewModel)
        }
        binding.option_menu.setOnClickListener { showMenuOption(binding, securityPreferences) }
    }

    private fun recyclerPoints(binding: View) {
        val recycler = binding.recycler_points
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = pointAdapter
    }

    private fun recyclerEmployee(binding: View, employeeAdapter: EmployeeAdapterHome) {
        val recycler = binding.recycler_employee_home
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = employeeAdapter
    }

    private fun salutation (binding: View) {

        val textName = binding.text_name_user
        val extras = securityPreferences.getStoredString(ConstantsUser.USER.COLUNAS.NAME)
        if (extras.isNotBlank()) { textName.text = extras }

        val horaCurrent = captureDateCurrent.captureHoraCurrent()
        val six = "06:00"
        val twelve = "12:00"
        val six2 = "18:00"

        binding.text_ola.text = when {
            horaCurrent > six && horaCurrent < twelve -> getString(R.string.bom_dia)
            horaCurrent > twelve && horaCurrent < six2 -> getString(R.string.boa_tarde)
            else -> getString(R.string.boa_noite)
        }
    }

    private fun searchPointsAndEmployee (binding: View, homeViewModel: InicioViewModel) {
        homeViewModel.getFullPoints("")
        homeViewModel.getFullEmployee()
        binding.progress.visibility = View.GONE
    }

    private fun observe(homeViewModel: InicioViewModel, employeeAdapter: EmployeeAdapterHome) {

        homeViewModel.pointsList.observe(viewLifecycleOwner, {
            when (it.size) {
                0 -> showSnackBar(R.string.precisa_add_funcionarios)
                else -> pointAdapter.updateFullPoints(it)
            }
        })

        homeViewModel.employeeList.observe(viewLifecycleOwner, {
            when (it.size) {
                0 -> showSnackBar(R.string.precisa_add_funcionarios)
                else -> employeeAdapter.updateEmployee(it)
            }
        })
    }

    private fun dialogLogout(securityPreferences: SecurityPreferences) {
        val alertDialog = createDialog(R.string.deseja_sair.toString())
        alertDialog?.setPositiveButton("Sim") { _, _ ->
            securityPreferences.removeString()
            onDetach()
        }
        alertDialog?.setNegativeButton("Não") { _, _ ->
            showSnackBar(R.string.cancelado)
        }
        alertDialog?.create()?.show()
    }

    private fun createDialog(message: String): AlertDialog.Builder? {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle(message)
        builder?.setCancelable(false)
        return builder
    }

    private fun dialogPoint (captureDateCurrent: CaptureDateCurrent, listEmployee: BusinessEmployee,
                             binding: View, homeViewModel: InicioViewModel) {

        val hourCurrent = captureDateCurrent.captureHoraCurrent()
        val dateCurrent = captureDateCurrent.captureDateCurrent()
        val list = listEmployee.consultEmployee()

        val inflate = layoutInflater.inflate(R.layout.dialog_bater_ponto, null)
        val textData = inflate.findViewById(R.id.dataPonto) as TextView
        textData.text = dateCurrent

        val listSpinner = inflate.findViewById(R.id.spinnerGetEmployee) as Spinner
        val adapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, list) }
        listSpinner.adapter = adapter
        listSpinner.onItemSelectedListener = this

        val alertDialog = createDialog("Bater Ponto")
        alertDialog?.setView(inflate)
        alertDialog?.setPositiveButton("Registrar") { _, _ ->

            when (val itemSpinner = listSpinner.selectedItem) {
                null -> { showSnackBar(R.string.precisa_add_funcionarios)}
                else -> savePoint(itemSpinner.toString(), dateCurrent, hourCurrent, binding, homeViewModel)
            }
        }
        alertDialog?.setNegativeButton(getString(R.string.cancelar)) { _, _ ->  }
        alertDialog?.create()?.show()
    }

    private fun savePoint (itemSpinner: String, dateCurrent: String, horaCurrent: String,
                           binding: View, homeViewModel: InicioViewModel){

        when{
            businessPoints.setPoints(itemSpinner, dateCurrent, horaCurrent) -> {
                searchPointsAndEmployee(binding, homeViewModel)
                showSnackBar(R.string.ponto_adicionado_sucesso)
            }
            else -> {
                showSnackBar(R.string.nao_possivel_add_ponto)
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent?.id) {
            R.id.spinnerGetEmployee -> { parent.getItemAtPosition(position).toString() }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun showMenuOption (binding: View, securityPreferences: SecurityPreferences) {

        val menuOption = PopupMenu(context, binding.option_menu)
        menuOption.menuInflater.inflate(R.menu.menu, menuOption.menu)
        menuOption.setOnMenuItemClickListener { item ->
            when (item!!.itemId) { R.id.logout_app_menu -> dialogLogout(securityPreferences) }
            true
        }
        menuOption.show()
    }

    override fun openFragmentProfile(employee: EmployeeEntity) {

        if (context is ItemEmployee) { listener = context as ItemEmployee
        } else { throw ClassCastException("$context must implemented") }
        listener.openFragment(employee)
    }

    private fun showSnackBar(message: Int) {
        Snackbar.make(container_home,
            message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(Color.BLACK)
            .setAction("Ok") {}
            .show()
    }

}