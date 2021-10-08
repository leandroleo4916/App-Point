package com.example.app_point.activitys

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.adapters.EmployeeAdapter
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.databinding.ActivityToolsBinding
import com.example.app_point.interfaces.OnItemClickRecycler
import com.example.app_point.model.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.recycler_employee.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class ToolsActivity : AppCompatActivity(), OnItemClickRecycler {

    private lateinit var mEmployeeAdapter: EmployeeAdapter
    private val vViewModelEmployee  by viewModel<ViewModelEmployee>()
    private val businessEmployee: BusinessEmployee by inject()
    private val binding by lazy { ActivityToolsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val recycler = findViewById<RecyclerView>(R.id.recycler_employee)
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
        vViewModelEmployee.getFullEmployee()
    }

    private fun observer(){
        val date = instanceCalendar()
        vViewModelEmployee.employeeFullList.observe(this, {
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
        val dateCalendar = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        return dateCalendar.format(calendar)
    }

    private fun removeEmployee(name: String){
        if (vViewModelEmployee.removeEmployee(name)){
            vViewModelEmployee.removePoints(name)
            showSnackBar(R.string.removido_sucesso)
            viewModel()
        }else {
            showSnackBar(R.string.nao_foi_possivel_remover)
        }
    }

    override fun clickEdit(position: Int) {
        val name = binding.recyclerEmployee[position].text_nome_employee.text.toString()
        val id = businessEmployee.consultIdEmployee(name)
        val intent = Intent(this, RegisterEmployeeActivity::class.java)
        intent.putExtra(ConstantsEmployee.EMPLOYEE.COLUMNS.ID, id)
        startActivity(intent)
        finish()
    }

    override fun clickRemove(position: Int) {
        val name = binding.recyclerEmployee[position].text_nome_employee.text.toString()
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Deseja remover $name?")
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Sim") { _, _ -> removeEmployee(name) }
        alertDialog.setNegativeButton("Não") { _, _ -> showSnackBar(R.string.cancelado) }
        val dialog = alertDialog.create()
        dialog.show()
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