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
import com.example.app_point.model.PointsAdapter
import com.example.app_point.model.ViewModel
import kotlinx.android.synthetic.main.activity_pontos.*

class PointsActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var mPointsAdapter: PointsAdapter
    private val mListEmployee: BusinessEmployee = BusinessEmployee(this)
    private lateinit var mViewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pontos)

        mViewModel = ViewModelProvider(this).get(ViewModel::class.java)

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
        mViewModel.employeeFullList.observe(this, { mPointsAdapter.updateFullEmployee(it)})
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
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        listSpinner.adapter = adapter
        listSpinner.onItemSelectedListener = this

        // Add Dialog
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.filtrar_funcionario))
        alertDialog.setView(inflateView)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton(getString(R.string.filtrar)) { _, _ ->

            // Capture item Spinner
            val itemSpinner = listSpinner.selectedItem.toString()

            mViewModel.getFullEmployee(itemSpinner)

        }
        alertDialog.setNegativeButton(getString(R.string.todos)) { _, _ ->
            mViewModel.getFullEmployee("")
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