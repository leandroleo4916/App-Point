package com.example.app_point.activitys

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.business.BusinessPoints
import com.example.app_point.constants.ConstantsUser
import com.example.app_point.model.PointsAdapter
import com.example.app_point.model.ViewModel
import com.example.app_point.utils.SecurityPreferences
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private val mListEmployee: BusinessEmployee = BusinessEmployee(this)
    private val mBusinessPoints: BusinessPoints = BusinessPoints(this)
    private lateinit var  mPointAdapter: PointsAdapter
    private lateinit var mSecurityPreferences: SecurityPreferences
    private lateinit var mViewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        animationIcons()

        mViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        mSecurityPreferences = SecurityPreferences(this)

        // Recycler Implementation
        val recycler = findViewById<RecyclerView>(R.id.recycler_points)
        recycler.layoutManager = LinearLayoutManager(this)
        mPointAdapter = PointsAdapter(application)
        recycler.adapter = mPointAdapter

        searchPoints()
        listener()
        observe()
        salutation()
    }

    // Animation to imageView
    private fun animationIcons(){
        val icon1 = findViewById<ImageView>(R.id.image_register_employee)
        val icon2 = findViewById<ImageView>(R.id.image_perfil_employee)
        val icon3 = findViewById<ImageView>(R.id.image_historicos_pontos)
        val icon4 = findViewById<ImageView>(R.id.image_opcoes)
        val icon5 = findViewById<ImageView>(R.id.image_in_register)
        val icon6 = findViewById<ImageView>(R.id.image_in_perfil)
        val icon7 = findViewById<ImageView>(R.id.image_in_clock)
        val icon8 = findViewById<ImageView>(R.id.image_in_opcoes)
        val animation: Animation = AnimationUtils.loadAnimation( application, R.anim.zoom)
        icon1.startAnimation(animation)
        icon2.startAnimation(animation)
        icon3.startAnimation(animation)
        icon4.startAnimation(animation)
        icon5.startAnimation(animation)
        icon6.startAnimation(animation)
        icon7.startAnimation(animation)
        icon8.startAnimation(animation)
    }

    // Shows admin name + greeting implement
    @SuppressLint("SimpleDateFormat")
    private fun salutation(){

        val extras = mSecurityPreferences.getStoredString(ConstantsUser.USER.COLUNAS.NAME)
        if (extras != "") {
            text_name_user.text = extras
        }
        
        val date = Calendar.getInstance().time
        val hora = SimpleDateFormat("HH:mm")
        val horaCurrent = hora.format(date)

        val clockSixMorning = "06:00"
        val clockTwelveMorning = "12:00"
        val clockSixEvening = "18:00"
        
        when {
            horaCurrent > clockSixMorning && horaCurrent < clockTwelveMorning -> { text_ola.text = getString(R.string.bom_dia) }
            horaCurrent > clockTwelveMorning && horaCurrent < clockSixEvening -> { text_ola.text = getString(R.string.boa_tarde) }
            else -> text_ola.text = getString(R.string.boa_noite)
        }
    }

    // Search Points DB
    private fun searchPoints(){
        Thread{
            // Block Thread
            Thread.sleep(500)
            runOnUiThread {
                mViewModel.getFullEmployee("")
                progress.visibility = View.GONE
            }
        }.start()
    }

    // Observe List Points
    private fun observe(){
        mViewModel.employeeFullList.observe(this, { mPointAdapter.updateFullEmployee(it) })
    }

    // clicks management
    private fun listener(){
        image_in_register.setOnClickListener(this)
        image_in_perfil.setOnClickListener(this)
        image_in_clock.setOnClickListener(this)
        image_in_opcoes.setOnClickListener(this)
        image_add_ponto.setOnClickListener(this)
        option_menu.setOnClickListener(this)
        float_bottom.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            image_in_register -> {
                startActivity(Intent(this, RegisterEmployeeActivity::class.java))
            }
            image_in_perfil -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            image_in_clock -> {
                startActivity(Intent(this, PointsActivity::class.java))
            }
            image_in_opcoes -> {
                startActivity(Intent(this, ToolsActivity::class.java))
            }
            image_add_ponto -> dialogPoint()
            option_menu -> showMenuOption()
            float_bottom -> dialogPoint()
        }
    }

    private fun dialogLogout(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.deseja_sair))
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Sim") { _, _ ->
            mSecurityPreferences.removeString()
            finish()
        }
        alertDialog.setNegativeButton("Não") { _, _ -> Toast.makeText(
            this, getString(R.string.cancelado), Toast.LENGTH_SHORT).show()
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
        val list = mListEmployee.consultEmployee()
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
            val itemSpinner = listSpinner.selectedItem.toString()
            savePoint(itemSpinner, dateCurrent, hourCurrent)

        }
        alertDialog.setNegativeButton(getString(R.string.cancelar)) { _, _ -> Toast.makeText(
            this,
            R.string.cancelado,
            Toast.LENGTH_SHORT).show()
        }
        val dialog = alertDialog.create()
        dialog.show()
    }

    // Send data captured to Business
    private fun savePoint(itemSpinner: String, dateCurrent: String, horaCurrent: String){
        when{
            itemSpinner == "" -> {
                Toast.makeText(this, getString(R.string.precisa_add_funcionarios), Toast.LENGTH_SHORT).show()
            }

            mBusinessPoints.getPoints(itemSpinner, dateCurrent, horaCurrent) -> {
                Toast.makeText(this, getString(R.string.adicionado_sucesso), Toast.LENGTH_SHORT).show()
                searchPoints()
            }
            else -> {
                Toast.makeText(this, getString(R.string.nao_possivel_add_ponto), Toast.LENGTH_SHORT).show()
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
        val menuOption = PopupMenu(this, option_menu)
        menuOption.menuInflater.inflate(R.menu.menu, menuOption.menu)
        menuOption.setOnMenuItemClickListener { item ->
            when (item!!.itemId) {
                R.id.profile_admin_menu -> {}
                R.id.employee_menu -> {}
                R.id.logout_app_menu -> dialogLogout()
            }
            true
        }
        menuOption.show()
    }
}