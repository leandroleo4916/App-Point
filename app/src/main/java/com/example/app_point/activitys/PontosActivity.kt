package com.example.app_point.activitys

import android.app.AlertDialog
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
import com.example.app_point.model.PontosAdapter
import com.example.app_point.model.ViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_pontos.*

class PontosActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var mPontosAdapter: PontosAdapter
    private val mListEmployee: BusinessEmployee = BusinessEmployee(this)
    private val mListPoint: BusinessPoints = BusinessPoints(this)
    private lateinit var mViewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pontos)

        mViewModel = ViewModelProvider(this).get(ViewModel::class.java)

        // Monta a recyclerview
        val recycler = findViewById<RecyclerView>(R.id.recycler_activity_pontos)
        recycler.layoutManager = LinearLayoutManager(this)
        mPontosAdapter = PontosAdapter(application)
        recycler.adapter = mPontosAdapter

        buscarPoints()
        listener()
        observe()
    }

    private fun buscarPoints(){
        Thread{
            Thread.sleep(1000)
            runOnUiThread {
                mViewModel.getEmployee()
                mViewModel.getData()
                mViewModel.getHora()
                progress_ponto.visibility = View.GONE
            }
        }.start()

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
        image_back_pontos.setOnClickListener(this)
        image_filter.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            image_back_pontos -> finish()
            image_filter -> dialogPoint()
        }
    }

    private fun dialogPoint(){
        val inflater = layoutInflater
        val inflate_view = inflater.inflate(R.layout.dialog_bater_ponto, null)

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
        alertDialog.setPositiveButton("Ok") {
                dialog, which ->

            // Captura item do Spinner
            val itemSpinner = listSpinner.selectedItem.toString()

        }
        alertDialog.setNegativeButton("Cancelar") {
                dialog, which -> Toast.makeText(this, "Cancelado!", Toast.LENGTH_SHORT).show()
        }
        val dialog = alertDialog.create()
        dialog.show()
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