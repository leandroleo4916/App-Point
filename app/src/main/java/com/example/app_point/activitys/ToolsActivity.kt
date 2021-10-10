package com.example.app_point.activitys

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_point.R
import com.example.app_point.adapters.EmployeeAdapter
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.databinding.ActivityToolsBinding
import com.example.app_point.interfaces.OnItemClickRecycler
import com.example.app_point.model.*
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class ToolsActivity : AppCompatActivity(), OnItemClickRecycler {

    private lateinit var mEmployeeAdapter: EmployeeAdapter
    private val viewModelEmployee by viewModel<ViewModelEmployee>()
    private val binding by lazy { ActivityToolsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val recycler = binding.recyclerEmployee
        recycler.layoutManager = LinearLayoutManager(this)
        mEmployeeAdapter = EmployeeAdapter(application, this)
        recycler.adapter = mEmployeeAdapter

        listener()
        viewModel()
        observer()
    }

    private fun listener(){
        binding.imageBackTools.setOnClickListener { finish() }
    }

    private fun viewModel(){
        viewModelEmployee.getFullEmployee()
    }

    private fun observer(){
        val date = instanceCalendar()
        viewModelEmployee.employeeFullList.observe(this, {
            when (it.size) {
                0 -> {
                    showSnackBar(R.string.precisa_add_funcionarios)
                    mEmployeeAdapter.updateFullEmployee(it)
                    binding.progressEmployee.visibility = View.GONE
                }
                else -> {
                    mEmployeeAdapter.updateFullEmployee(it)
                    mEmployeeAdapter.updateDateCurrent(date)
                    binding.progressEmployee.visibility = View.GONE
                }
            }
        })
    }

    private fun instanceCalendar(): String{
        val calendar = Calendar.getInstance().time
        val local = Locale("pt", "BR")
        val dateCalendar = SimpleDateFormat("dd/MM/yyyy", local)
        return dateCalendar.format(calendar)
    }

    override fun clickEdit(id: Int) {
        val intent = Intent(this, RegisterEmployeeActivity::class.java)
        intent.putExtra(ConstantsEmployee.EMPLOYEE.COLUMNS.ID, id)
        startActivity(intent)
        finish()
    }

    override fun clickRemove(id: Int, name: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Deseja remover $name?")
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Sim") { _, _ -> removeEmployee(id, name) }
        alertDialog.setNegativeButton("Não") { _, _ -> showSnackBar(R.string.cancelado) }
        val dialog = alertDialog.create()
        dialog.show()
    }

    private fun removeEmployee(id: Int, name: String){
        if (viewModelEmployee.removeEmployee(id)){
            viewModelEmployee.removePoints(name)
            showSnackBar(R.string.removido_sucesso)
            viewModel()
        }else {
            showSnackBar(R.string.nao_foi_possivel_remover)
        }
    }

    private fun showSnackBar(message: Int) {
        Snackbar.make(binding.containerEmployee,
            message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(Color.BLACK)
            .setAction("Ok") {}
            .show()
    }
}