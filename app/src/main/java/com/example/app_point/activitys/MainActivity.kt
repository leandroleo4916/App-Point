package com.example.app_point.activitys

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.business.BusinessPoints
import com.example.app_point.model.ViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private val mListEmployee: BusinessEmployee = BusinessEmployee(this)
    private val mBusinessPoints: BusinessPoints = BusinessPoints(this)
    private val mPontosAdapter: PontosAdapter = PontosAdapter()
    private val mViewModel: ViewModel = ViewModel(application)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1 - Captura a recycler
        val recycler = findViewById<RecyclerView>(R.id.recycler_points)
        // 2 - Adiciona o Layout
        recycler.layoutManager = LinearLayoutManager(this)
        // 3 - Implementa o modelo layout
        recycler.adapter = mPontosAdapter

        mViewModel.getEmployee()
        mViewModel.getData()
        mViewModel.getHora()

        listener()
        observe()
    }

    private fun observe(){
        mViewModel.employeeList.observe(this, androidx.lifecycle.Observer {
            mPontosAdapter.updateFuncionario(it)
        })
        mViewModel.dataList.observe(this, androidx.lifecycle.Observer {
            mPontosAdapter.updateData(it)
        })
        mViewModel.horaList.observe(this, androidx.lifecycle.Observer {
            mPontosAdapter.updateHora(it)
        })
    }

    private fun listener(){
        image_in_register.setOnClickListener(this)
        image_in_perfil.setOnClickListener(this)
        image_in_clock.setOnClickListener(this)
        image_in_opcoes.setOnClickListener(this)
        image_add_ponto.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            image_in_register -> {startActivity(Intent(this, RegisterEmployeeActivity::class.java))}
            image_in_perfil -> {startActivity(Intent(this, PerfilActivity::class.java))}
            image_in_clock -> {startActivity(Intent(this, PontosActivity::class.java))}
            image_in_opcoes -> {startActivity(Intent(this, ToolsActivity::class.java))}
            image_add_ponto -> dialogPoint()
        }
    }

    private fun dialogPoint(){
        val date = Calendar.getInstance().time
        val dateTime = SimpleDateFormat("d/MM/YYYY", Locale.ENGLISH)

        // Pegando hora atual
        val hora = SimpleDateFormat("HH:mm")
        val horaAtual: String = hora.format(date)

        // Pegando data atual
        val dataAtual = dateTime.format(date)

        val inflater = layoutInflater
        val inflate_view = inflater.inflate(R.layout.dialog_bater_ponto, null)
        val textData = inflate_view.findViewById(R.id.dataPonto) as TextView
        textData.text = dateTime.format(date)

        // Capturando Lista de Funcionarios e adiciona ao spinner
        val list = mListEmployee.consultEmployee()
        val listSpinner= inflate_view.findViewById(R.id.spinnerGetFuncionario) as Spinner
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

    private fun savePoint(itemSpinner: String, dateAtual: String, horaAtual: String){
        when{
            itemSpinner == "" -> {
                Toast.makeText(this, "Você precisa adicionar funcionários!", Toast.LENGTH_SHORT).show()
            }
            mBusinessPoints.getPoints(itemSpinner, dateAtual, horaAtual) ->
                Toast.makeText(this, "Adicionado com sucesso!", Toast.LENGTH_SHORT).show()

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