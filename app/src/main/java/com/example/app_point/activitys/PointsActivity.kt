package com.example.app_point.activitys

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.model.PointsAdapter
import com.example.app_point.model.ViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_pontos.*

class PointsActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var mPointsAdapter: PointsAdapter
    private val mListEmployee: BusinessEmployee = BusinessEmployee(this)
    private lateinit var mViewModel: ViewModel
    private lateinit var constraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pontos)

        mViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        constraintLayout = findViewById(R.id.container_pontos)

        // Create the recyclerview
        val recycler = findViewById<RecyclerView>(R.id.recycler_activity_pontos)
        recycler.layoutManager = LinearLayoutManager(this)
        mPointsAdapter = PointsAdapter(application)
        recycler.adapter = mPointsAdapter

        searchPoints()
        listener()
        observe()
    }

    private fun searchPoints(){
        Thread{
            // Block Thread
            Thread.sleep(500)
            runOnUiThread {
                mViewModel.getFullEmployee("")
                progress_ponto.visibility = View.GONE
            }
        }.start()
    }

    private fun observe(){
        mViewModel.employeeFullList.observe(this, {
            when (it.size) {
                0 -> { Snackbar.make(constraintLayout, getString(R.string.nenhum_ponto_registrado),
                    Snackbar.LENGTH_LONG).show() }
                else -> { mPointsAdapter.updateFullEmployee(it) }
            }
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
        val inflateView = inflater.inflate(R.layout.dialog_list_employee, null)

        // Capture List employee and add spinner
        val list = mListEmployee.consultEmployee()
        val listSpinner = inflateView.findViewById(R.id.spinner_employee) as Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list!!)
        listSpinner.adapter = adapter
        listSpinner.onItemSelectedListener = this

        // Add Dialog
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.filtrar_funcionario))
        alertDialog.setView(inflateView)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton(getString(R.string.filtrar)) { _, _ ->

            // Capture item Spinner
            when (val itemSpinner = listSpinner.selectedItem) {
                null -> { Snackbar.make(constraintLayout, getString(R.string.precisa_add_funcionarios),
                    Snackbar.LENGTH_LONG).show() }
                else -> { mViewModel.getFullEmployee(itemSpinner.toString()) }
            }
        }
        alertDialog.setNegativeButton(getString(R.string.todos)) { _, _ ->
            when (listSpinner.selectedItem) {
                null -> { Snackbar.make(constraintLayout, getString(R.string.precisa_add_funcionarios), Snackbar.LENGTH_LONG).show() }
                else -> { mViewModel.getFullEmployee("") }
            }

        }
        alertDialog.create().show()
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