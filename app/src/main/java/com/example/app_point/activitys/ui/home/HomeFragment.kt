package com.example.app_point.activitys.ui.home

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_point.R
import com.example.app_point.adapters.EmployeeAdapterHome
import com.example.app_point.adapters.PointsAdapter
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.constants.ConstantsUser
import com.example.app_point.interfaces.*
import com.example.app_point.utils.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class HomeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var listener: ItemClickOpenProfileById
    private lateinit var hideNav: IHideNavView
    private lateinit var logoutApp: ILogoutApp
    private lateinit var itemClickOpenRegister: ItemClickOpenRegister
    private val pointAdapter: PointsAdapter by inject()
    private val businessEmployee: BusinessEmployee by inject()
    private val converterPhoto: ConverterPhoto by inject()
    private val captureDateCurrent: CaptureDateCurrent by inject()
    private val securityPreferences: SecurityPreferences by inject()
    private val homeViewModel: HomeViewModel by viewModel()
    private val color: GetColor by inject()
    private lateinit var employeeAdapter: EmployeeAdapterHome
    private lateinit var binding: View
    private val delay: Long = 0
    private val timer = Timer()

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = inflater.inflate(R.layout.fragment_home, container, false)

        if (context is ItemClickOpenProfileById) { listener = context as ItemClickOpenProfileById }
        employeeAdapter = EmployeeAdapterHome(listener, color, converterPhoto)

        updateHourAndDate()
        recyclerPoints()
        recyclerEmployee()
        searchPointsAndEmployee()
        salutation()
        observe()
        listener()
        showNavView()

        return binding
    }

    private fun updateHourAndDate() {

        binding.textView_date_current.text = captureDateCurrent.captureDateCurrent()

        val interval: Long = 5000
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                CoroutineScope(Dispatchers.Main).launch {
                    animationFab()
                }
            }
        }, delay, interval)

        val intervalSecond: Long = 1000
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.textView_hour_current.text =
                        captureDateCurrent.captureHoraCurrentSecond()
                }
            }
        }, delay, intervalSecond)
    }

    private fun animationFab() {
        val animatorSet = AnimatorSet()
        animatorSet.duration = 800
        val animatorList = ArrayList<Animator>()
        val scaleXAnimator = ObjectAnimator.ofFloat(
            binding.fab_add_point, "ScaleX", 1f, 1.2f, 1f)
        animatorList.add(scaleXAnimator)
        val scaleYAnimator = ObjectAnimator.ofFloat(
            binding.fab_add_point, "ScaleY", 1f, 1.2f, 1f)
        animatorList.add(scaleYAnimator)
        animatorSet.playTogether(animatorList)
        animatorSet.start()
    }

    private fun showNavView() {

        var isShow = true
        var scrollRange = -1
        val appBar = binding.appbar
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) { scrollRange = barLayout?.totalScrollRange!! }
            if (scrollRange + verticalOffset == 0) {

                if (context is ItemEmployee) { hideNav = context as IHideNavView }
                hideNav.hideNavView(false)
                isShow = true
            } else if (isShow) {

                if (context is ItemEmployee) { hideNav = context as IHideNavView }
                hideNav.hideNavView(true)
                isShow = false
            }
        })
    }

    private fun listener() {
        binding.fab_add_point.setOnClickListener { dialogRegisterPoint() }
        binding.logout_app.setOnClickListener { dialogLogout() }
        binding.textView_add_employee.setOnClickListener {
            if (context is ItemClickOpenRegister) {
                itemClickOpenRegister = context as ItemClickOpenRegister
                itemClickOpenRegister.openFragmentRegister()
            }
        }
        binding.imageView_add_employee.setOnClickListener {
            if (context is ItemClickOpenRegister) {
                itemClickOpenRegister = context as ItemClickOpenRegister
                itemClickOpenRegister.openFragmentRegister()
            }
        }
    }

    private fun recyclerPoints() {
        val recycler = binding.recycler_points
        recycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = pointAdapter
    }

    private fun recyclerEmployee() {
        val recycler = binding.recycler_employee_home
        recycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = employeeAdapter
    }

    private fun salutation() {

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

    private fun searchPointsAndEmployee() {
        homeViewModel.getFullPoints(0)
        binding.textView_add_points.visibility = View.GONE

        homeViewModel.getFullEmployee()
        binding.imageView_add_employee.visibility = View.GONE
        binding.textView_add_employee.visibility = View.GONE
    }

    private fun observe() {
        homeViewModel.pointsList.observe(viewLifecycleOwner, {
            when (it.size) {
                0 -> {
                    binding.progress.visibility = View.GONE
                    binding.textView_add_points.visibility = View.VISIBLE
                }
                else -> {
                    pointAdapter.updateFullPoints(it)
                    binding.progress.visibility = View.GONE
                    binding.textView_add_points.visibility = View.GONE
                }
            }
        })
        homeViewModel.employeeList.observe(viewLifecycleOwner, {
            when (it.size) {
                0 -> {
                    binding.progress_add_employee.visibility = View.GONE
                    binding.imageView_add_employee.visibility = View.VISIBLE
                    binding.textView_add_employee.visibility = View.VISIBLE
                }
                else -> {
                    employeeAdapter.updateEmployee(it)
                    binding.progress_add_employee.visibility = View.GONE
                    binding.imageView_add_employee.visibility = View.GONE
                    binding.textView_add_employee.visibility = View.GONE
                }
            }
        })
    }

    private fun dialogLogout() {

        val alertDialog = createDialog(getString(R.string.sair_app))
        alertDialog?.setPositiveButton("Sim") { _, _ ->
            securityPreferences.removeString()
            if (context is ILogoutApp) { logoutApp = context as ILogoutApp }
            logoutApp.logoutApp()
        }
        alertDialog?.setNegativeButton("Não") { _, _ -> }
        alertDialog?.create()?.show()
    }

    private fun createDialog(message: String): AlertDialog.Builder? {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle(message)
        builder?.setCancelable(false)
        return builder
    }

    private fun dialogRegisterPoint () {

        val hourCurrent = captureDateCurrent.captureHoraCurrent()
        val dateCurrent = captureDateCurrent.captureDateCurrent()
        val list = businessEmployee.consultEmployeeList()

        val inflate = layoutInflater.inflate(R.layout.dialog_bater_ponto, null)
        val listSpinner = inflate.findViewById(R.id.spinnerGetEmployee) as Spinner
        val adapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, list) }
        listSpinner.adapter = adapter
        listSpinner.onItemSelectedListener = this

        val alertDialog = createDialog("Registrar Ponto")
        alertDialog?.setView(inflate)
        alertDialog?.setPositiveButton("Registrar") { _, _ ->

            when (val itemSpinner = listSpinner.selectedItem) {
                null -> {
                    showSnackBar(R.string.precisa_add_funcionarios)
                }
                else -> {
                    val id = businessEmployee.consultIdEmployeeByName(itemSpinner.toString())
                    savePoint(id, itemSpinner.toString(), dateCurrent, hourCurrent)
                }
            }
        }
        alertDialog?.setNegativeButton(getString(R.string.cancelar)) { _, _ -> }
        alertDialog?.create()?.show()
    }

    private fun savePoint(id: Int, itemSpinner: String, dateCurrent: String, horaCurrent: String){
        when{
            homeViewModel.setPoints(id, itemSpinner, dateCurrent, horaCurrent) -> {
                searchPointsAndEmployee()
                showSnackBar(R.string.ponto_adicionado_sucesso)
            }
            else -> showSnackBar(R.string.nao_possivel_add_ponto)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent?.id) {
            R.id.spinnerGetEmployee -> {
                parent.getItemAtPosition(position).toString()
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun showSnackBar(message: Int) {
        Snackbar.make(binding.container_home,
            message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(Color.BLACK)
            .setAction("Ok") {}
            .show()
    }
}