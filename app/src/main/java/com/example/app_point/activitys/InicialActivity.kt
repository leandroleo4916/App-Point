package com.example.app_point.activitys

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
import com.example.app_point.adapters.PointsAdapter
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.business.BusinessPoints
import com.example.app_point.constants.ConstantsUser
import com.example.app_point.databinding.ActivityInicialBinding
import com.example.app_point.model.ViewModelMain
import com.example.app_point.repository.RepositoryPoint
import com.example.app_point.utils.CaptureDateCurrent
import com.example.app_point.utils.SecurityPreferences
import com.example.app_point.utils.createDialog
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

class InicialActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val listEmployee: BusinessEmployee by inject()
    private val captureDateCurrent: CaptureDateCurrent by inject()
    private val businessPoints: BusinessPoints by inject()
    private val pointAdapter: PointsAdapter by inject()
    private val repositoryPoint: RepositoryPoint by inject()
    private val securityPreferences: SecurityPreferences by inject()
    private lateinit var viewModelMain: ViewModelMain
    private val binding by lazy { ActivityInicialBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        animationIcons()
        viewModelMain = ViewModelMain(application, repositoryPoint)

        recycler()
        searchPoints()
        listener()
        observe()
        salutation()
    }

    private fun recycler(){
        val recycler = binding.recyclerPoints
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = pointAdapter
    }

    private fun animationIcons(){
        val animation: Animation = AnimationUtils.loadAnimation(application, R.anim.animation_down)
        binding.run {
            imagePerfilEmployee.startAnimation(animation)
            imageHistoricosPontos.startAnimation(animation)
            imageOpcoes.startAnimation(animation)
            imageInRegister.startAnimation(animation)
            imageInPerfil.startAnimation(animation)
            imageInClock.startAnimation(animation)
            imageInOpcoes.startAnimation(animation)
        }
    }

    private fun salutation(){

        val extras = securityPreferences.getStoredString(ConstantsUser.USER.COLUNAS.NAME)
        if (extras.isNotBlank()) { binding.textNameUser.text = extras }

        val horaCurrent = captureDateCurrent.captureHoraCurrent()
        val clockSix = "06:00"
        val clockTwelve = "12:00"
        val clockSix2 = "18:00"

        when {
            horaCurrent > clockSix && horaCurrent < clockTwelve -> {
                binding.textOla.text = getString(R.string.bom_dia) }
            horaCurrent > clockTwelve && horaCurrent < clockSix2 -> {
                binding.textOla.text = getString(R.string.boa_tarde) }
            else ->
                binding.textOla.text = getString(R.string.boa_noite)
        }
    }

    private fun searchPoints(){
        viewModelMain.getFullEmployee("")
        binding.progress.visibility = View.GONE
    }

    private fun observe(){
        viewModelMain.employeeFullList.observe(this, {
            when (it.size) {
                0 -> showSnackBar(R.string.nenhum_ponto_registrado)
                else -> {
                    pointAdapter.updateFullPoints(it)
                }
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
        binding.imageInClock.setOnClickListener{  }
        binding.imageInOpcoes.setOnClickListener{
            startActivity(Intent(this, ToolsActivity::class.java))
        }
        binding.imageAddPonto.setOnClickListener { dialogPoint() }
        binding.optionMenu.setOnClickListener { showMenuOption() }
    }

    private fun dialogLogout(){
        val alertDialog = createDialog(R.string.deseja_sair.toString())
        alertDialog.setPositiveButton("Sim") { _, _ ->
            securityPreferences.removeString()
            finish()
        }
        alertDialog.setNegativeButton("Não") { _, _ -> showSnackBar(R.string.cancelado) }
        alertDialog.create().show()
    }

    private fun dialogPoint(){

        val hourCurrent = captureDateCurrent.captureHoraCurrent()
        val dateCurrent = captureDateCurrent.captureDateCurrent()
        val list = listEmployee.consultEmployee()

        val inflate = layoutInflater.inflate(R.layout.dialog_bater_ponto, null)
        val textData = inflate.findViewById(R.id.dataPonto) as TextView
        textData.text = dateCurrent

        val listSpinner = inflate.findViewById(R.id.spinnerGetEmployee) as Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        listSpinner.adapter = adapter
        listSpinner.onItemSelectedListener = this

        val alertDialog = createDialog("Bater Ponto")
        alertDialog.setView(inflate)
        alertDialog.setPositiveButton("Registrar") { _, _ ->

            when (val itemSpinner = listSpinner.selectedItem) {
                null -> showSnackBar(R.string.precisa_add_funcionarios)
                else -> savePoint(itemSpinner.toString(), dateCurrent, hourCurrent)
            }
        }
        alertDialog.setNegativeButton(getString(R.string.cancelar)) { _, _ -> showSnackBar(R.string.cancelado) }
        alertDialog.create().show()
    }

    private fun savePoint(itemSpinner: String, dateCurrent: String, horaCurrent: String){

        when{
            businessPoints.setPoints(itemSpinner, dateCurrent, horaCurrent) -> {
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
            R.id.spinnerGetEmployee -> {
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