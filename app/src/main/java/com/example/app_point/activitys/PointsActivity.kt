package com.example.app_point.activitys

import android.app.AlertDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.databinding.ActivityPontosBinding
import com.example.app_point.model.PointsAdapter
import com.example.app_point.model.ViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PointsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val mPointsAdapter: PointsAdapter by inject()
    private val mListEmployee: BusinessEmployee by inject()
    private val mViewModel: ViewModel by viewModel()
    private val binding by lazy { ActivityPontosBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val recycler = binding.recyclerActivityPontos
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = mPointsAdapter

        searchPoints()
        listener()
        observe()
    }

    private fun searchPoints(){
        mViewModel.getFullEmployee("")
        binding.progressPonto.visibility = View.GONE
    }

    private fun observe(){
        mViewModel.employeeFullList.observe(this, {
            when (it.size) {
                0 -> showSnackBar(R.string.nenhum_ponto_registrado)
                else -> { mPointsAdapter.updateFullEmployee(it) }
            }
        })
    }

    private fun listener(){
        binding.imageBackPontos.setOnClickListener{
            finish()
        }
        binding.imageFilter.setOnClickListener{
            dialogPoint()
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
            when (val itemSpinner = listSpinner.selectedItem) {
                null -> showSnackBar(R.string.precisa_add_funcionarios)
                else -> { mViewModel.getFullEmployee(itemSpinner.toString()) }
            }
        }
        alertDialog.setNegativeButton(getString(R.string.todos)) { _, _ ->
            when (listSpinner.selectedItem) {
                null -> showSnackBar(R.string.precisa_add_funcionarios)
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

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun showSnackBar(message: Int) {
        Snackbar.make(binding.containerPontos,
            message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(Color.BLACK)
            .setAction("Ok") {}
            .show()
    }
}