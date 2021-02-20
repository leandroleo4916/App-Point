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
import com.example.app_point.model.PontosAdapter
import com.example.app_point.model.ViewModel
import kotlinx.android.synthetic.main.activity_pontos.*

class PontosActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var mPontosAdapter: PontosAdapter
    private val mListEmployee: BusinessEmployee = BusinessEmployee(this)
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
            // Block Thread
            Thread.sleep(500)
            runOnUiThread {
                mViewModel.getEmployee("")
                mViewModel.getData("")
                mViewModel.getHora("")
                progress_ponto.visibility = View.GONE
            }
        }.start()
    }

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
        val inflateView = inflater.inflate(R.layout.dialog_bater_ponto, null)

        // Capture List employee and add spinner
        val list = mListEmployee.consultEmployee()
        val listSpinner = inflateView.findViewById(R.id.spinnerGetFuncionario) as Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        listSpinner.adapter = adapter
        listSpinner.onItemSelectedListener = this

        // Add Dialog
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Bater Ponto")
        alertDialog.setView(inflateView)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Ok") { _, _ ->

            // Capture item Spinner
            val itemSpinner = listSpinner.selectedItem.toString()

            mViewModel.getEmployee(itemSpinner)
            mViewModel.getData(itemSpinner)
            mViewModel.getHora(itemSpinner)

        }
        alertDialog.setNegativeButton(R.string.cancelar) { _, _ ->
            Toast.makeText(this, R.string.cancelado, Toast.LENGTH_SHORT).show()
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

    }
}