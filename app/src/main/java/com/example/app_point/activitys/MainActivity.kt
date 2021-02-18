package com.example.app_point.activitys

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.business.BusinessPoints
import com.example.app_point.constants.ConstantsUser
import com.example.app_point.model.PontosAdapter
import com.example.app_point.model.ViewModel
import com.example.app_point.utils.SecurityPreferences
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private val mListEmployee: BusinessEmployee = BusinessEmployee(this)
    private val mBusinessPoints: BusinessPoints = BusinessPoints(this)
    private lateinit var  mPontosAdapter: PontosAdapter
    private lateinit var mSecurityPreferences: SecurityPreferences
    private lateinit var mViewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        mSecurityPreferences = SecurityPreferences(this)

        // Implementação da recycler
        val recycler = findViewById<RecyclerView>(R.id.recycler_points)
        recycler.layoutManager = LinearLayoutManager(this)
        mPontosAdapter = PontosAdapter(application)
        recycler.adapter = mPontosAdapter

        buscarPoints()
        listener()
        observe()
        modifyName()
    }

    // Recebe nome do Usuário Administrador e deixa visível no Toolbar
    private fun modifyName(){
        val extras = mSecurityPreferences.getStoredString(ConstantsUser.USER.COLUNAS.NAME)
        if (extras != "") {
            text_name_user.text = extras
        }
    }

    // Busca pontos no Banco de Dados
    private fun buscarPoints(){
        mViewModel.getEmployee()
        mViewModel.getData()
        mViewModel.getHora()
    }

    // Observe as Listas de Pontos Batidos
    private fun observe(){
        mViewModel.employeeList.observe(this, {
            mPontosAdapter.updateFuncionario(it)
        })
        mViewModel.dataList.observe(this, {
            mPontosAdapter.updateData(it)
        })
        mViewModel.horaList.observe(this, {
            mPontosAdapter.updateHora(it)
        })
    }

    // Gerencia os clicks
    private fun listener(){
        image_in_register.setOnClickListener(this)
        image_in_perfil.setOnClickListener(this)
        image_in_clock.setOnClickListener(this)
        image_in_opcoes.setOnClickListener(this)
        image_add_ponto.setOnClickListener(this)
        text_logout.setOnClickListener(this)
        float_bottom.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            image_in_register -> {startActivity(Intent(this, RegisterEmployeeActivity::class.java))}
            image_in_perfil -> {startActivity(Intent(this, PerfilActivity::class.java))}
            image_in_clock -> {startActivity(Intent(this, PontosActivity::class.java))}
            image_in_opcoes -> {startActivity(Intent(this, ToolsActivity::class.java))}
            image_add_ponto -> dialogPoint()
            text_logout -> dialogLogout()
            float_bottom -> dialogPoint()
        }
    }

    private fun dialogLogout(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Deseja sair do App?")
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Sim") {
                dialog, which ->
            mSecurityPreferences.removeString()
            finish()
        }
        alertDialog.setNegativeButton("Não") {
                dialog, which -> Toast.makeText(this, "Cancelado!", Toast.LENGTH_SHORT).show()
        }
        val dialog = alertDialog.create()
        dialog.show()
    }

    private fun dialogPoint(){
        val date = Calendar.getInstance().time
        val dateTime = SimpleDateFormat("d/MM/YYYY", Locale.ENGLISH)

        // Captura hora atual
        val hora = SimpleDateFormat("HH:mm")
        val horaAtual: String = hora.format(date)

        // Captura data atual
        val dataAtual = dateTime.format(date)

        val inflater = layoutInflater
        val inflate_view = inflater.inflate(R.layout.dialog_bater_ponto, null)
        val textData = inflate_view.findViewById(R.id.dataPonto) as TextView
        textData.text = dataAtual

        // Capturando Lista de Funcionarios e adiciona ao spinner
        val list = mListEmployee.consultEmployee()
        val listSpinner = inflate_view.findViewById(R.id.spinnerGetFuncionario) as Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        listSpinner.adapter = adapter
        listSpinner.onItemSelectedListener = this

        // Cria o Dialog
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Bater Ponto")
        alertDialog.setView(inflate_view)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Registrar") {
                dialog, which ->

            // Captura item do Spinner
            val itemSpinner = listSpinner.selectedItem.toString()
            savePoint(itemSpinner, dataAtual, horaAtual)

        }
        alertDialog.setNegativeButton("Cancelar") {
                dialog, which -> Toast.makeText(this, "Cancelado!", Toast.LENGTH_SHORT).show()
        }
        val dialog = alertDialog.create()
        dialog.show()
    }

    // Envia dados capturados para a Business
    private fun savePoint(itemSpinner: String, dateAtual: String, horaAtual: String){
        when{
            itemSpinner == "" -> {
                Toast.makeText(this, "Você precisa adicionar funcionários!", Toast.LENGTH_SHORT).show()
            }
            mBusinessPoints.getPoints(itemSpinner, dateAtual, horaAtual) -> {
                Toast.makeText(this, "Adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                buscarPoints()
            }
            else -> {
                Toast.makeText(this, "Não foi possível adicionar ponto!", Toast.LENGTH_SHORT).show()
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
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}