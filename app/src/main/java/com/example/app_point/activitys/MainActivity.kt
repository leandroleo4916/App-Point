package com.example.app_point.activitys

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.business.BusinessPoints
import com.example.app_point.constants.ConstantsUser
import com.example.app_point.databinding.ActivityMainBinding
import com.example.app_point.model.PointsAdapter
import com.example.app_point.model.ViewModel
import com.example.app_point.utils.SecurityPreferences
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val listEmployee: BusinessEmployee by inject()
    private val businessPoints: BusinessPoints by inject()
    private val pointAdapter: PointsAdapter by inject()
    private val securityPreferences: SecurityPreferences by inject()
    private val vViewModel by viewModel<ViewModel>()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        animationIcons()

        val recycler = binding.recyclerPoints
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = pointAdapter

        searchPoints()
        listener()
        observe()
        salutation()
    }

    // Animation to imageView
    private fun animationIcons(){
        val animation: Animation = AnimationUtils.loadAnimation( application, R.anim.zoom)
        binding.imageRegisterEmployee.startAnimation(animation)
        binding.imagePerfilEmployee.startAnimation(animation)
        binding.imageHistoricosPontos.startAnimation(animation)
        binding.imageOpcoes.startAnimation(animation)
        binding.imageInRegister.startAnimation(animation)
        binding.imageInPerfil.startAnimation(animation)
        binding.imageInClock.startAnimation(animation)
        binding.imageInOpcoes.startAnimation(animation)
    }

    private fun salutation(){

        val extras = securityPreferences.getStoredString(ConstantsUser.USER.COLUNAS.NAME)
        if (extras.isNotBlank()) {
            binding.textNameUser.text = extras
        }

        val date = Calendar.getInstance().time
        val local = Locale("pt", "BR")
        val hora = SimpleDateFormat("HH:mm", local)
        val horaCurrent = hora.format(date)

        val clockSixMorning = "06:00"
        val clockTwelveMorning = "12:00"
        val clockSixEvening = "18:00"

        when {
            horaCurrent > clockSixMorning && horaCurrent < clockTwelveMorning -> {
                binding.textOla.text = getString(R.string.bom_dia) }
            horaCurrent > clockTwelveMorning && horaCurrent < clockSixEvening -> {
                binding.textOla.text = getString(R.string.boa_tarde) }
            else ->
                binding.textOla.text = getString(R.string.boa_noite)
        }
    }

    private fun searchPoints(){
        vViewModel.getFullEmployee("")
        binding.progress.visibility = View.GONE
    }

    private fun observe(){
        vViewModel.employeeFullList.observe(this, {
            when (it.size) {
                0 -> showSnackBar(R.string.nenhum_ponto_registrado)
                else -> { pointAdapter.updateFullEmployee(it) }
            }
        })
    }

    private fun listener(){
        binding.imageInRegister.setOnClickListener {
            startActivity(Intent(this, RegisterEmployeeActivity::class.java))
        }
        binding.imageInPerfil.setOnClickListener{
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        binding.imageInClock.setOnClickListener{
            startActivity(Intent(this, PointsActivity::class.java))
        }
        binding.imageInOpcoes.setOnClickListener{
            startActivity(Intent(this, ToolsActivity::class.java))
        }
        binding.imageAddPonto.setOnClickListener { dialogPoint() }
        binding.optionMenu.setOnClickListener { showMenuOption() }
    }

    private fun dialogLogout(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.deseja_sair))
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Sim") { _, _ ->
            securityPreferences.removeString()
            finish()
        }
        alertDialog.setNegativeButton("Não") { _, _ ->
            showSnackBar(R.string.cancelado)
        }
        val dialog = alertDialog.create()
        dialog.show()
    }

    @SuppressLint("SimpleDateFormat", "WeekBasedYear")
    private fun dialogPoint(){
        val date = Calendar.getInstance().time
        val dateTime = SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH)

        // Captures current hour
        val hora = SimpleDateFormat("HH:mm")
        val hourCurrent = hora.format(date)

        // Captures current date
        val dateCurrent = dateTime.format(date)

        val inflater = layoutInflater
        val inflateView = inflater.inflate(R.layout.dialog_bater_ponto, null)
        val textData = inflateView.findViewById(R.id.dataPonto) as TextView
        textData.text = dateCurrent

        // Captures employee list and add to spinner
        val list = listEmployee.consultEmployee()
        val listSpinner = inflateView.findViewById(R.id.spinnerGetFuncionario) as Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        listSpinner.adapter = adapter
        listSpinner.onItemSelectedListener = this

        // Create the Dialog
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Bater Ponto")
        alertDialog.setView(inflateView)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Registrar") { _, _ ->

            // Captures item do Spinner
            when (val itemSpinner = listSpinner.selectedItem) {
                null -> showSnackBar(R.string.precisa_add_funcionarios)
                else -> { savePoint(itemSpinner.toString(), dateCurrent, hourCurrent) }
            }
        }
        alertDialog.setNegativeButton(getString(R.string.cancelar)) { _, _ ->
            showSnackBar(R.string.cancelado)
        }
        val dialog = alertDialog.create()
        dialog.show()
    }

    private fun savePoint(itemSpinner: String, dateCurrent: String, horaCurrent: String){

        when{
            businessPoints.getPoints(itemSpinner, dateCurrent, horaCurrent) -> {
                showSnackBar(R.string.adicionado_sucesso)
                searchPoints()
            }
            else -> {
                showSnackBar(R.string.nao_possivel_add_ponto)
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent?.id) {
            R.id.spinnerGetFuncionario -> {
                parent.getItemAtPosition(position).toString()
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun showMenuOption(){
        val menuOption = PopupMenu(this, binding.optionMenu)
        menuOption.menuInflater.inflate(R.menu.menu, menuOption.menu)
        menuOption.setOnMenuItemClickListener { item ->
            when (item!!.itemId) {
                R.id.employee_menu -> startActivity(Intent(this,
                    ToolsActivity::class.java))
                R.id.logout_app_menu -> dialogLogout()
            }
            true
        }
        menuOption.show()
    }

    private fun showSnackBar(message: Int) {
        Snackbar.make(binding.coordinator,
            message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(Color.BLACK)
            .setAction("Ok") {}
            .show()
    }
}