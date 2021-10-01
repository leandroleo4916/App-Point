package com.example.app_point.activitys

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.interfaces.OnItemClickRecycler
import com.example.app_point.model.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_tools.*
import kotlinx.android.synthetic.main.recycler_employee.view.*
import java.text.SimpleDateFormat
import java.util.*

class ToolsActivity : AppCompatActivity(), OnItemClickRecycler {

    private lateinit var mEmployeeAdapter: EmployeeAdapter
    private lateinit var mViewModelEmployee: ViewModelEmployee
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var businessEmployee: BusinessEmployee

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tools)

        mViewModelEmployee = ViewModelProvider(this).get(ViewModelEmployee::class.java)
        constraintLayout = findViewById(R.id.container_employee)
        businessEmployee = BusinessEmployee(this)

        val recycler = findViewById<RecyclerView>(R.id.recycler_employee)
        recycler.layoutManager = LinearLayoutManager(this)
        mEmployeeAdapter = EmployeeAdapter(application, this)
        recycler.adapter = mEmployeeAdapter

        listener()
        viewModel()
        observer()
    }

    private fun listener(){
        image_back_tools.setOnClickListener { finish() }
    }

    private fun viewModel(){
        mViewModelEmployee.getFullEmployee()
    }

    private fun observer(){
        val date = instanceCalendar()
        mViewModelEmployee.employeeFullList.observe(this, {
            when (it.size) {
                0 -> { showSnackBar(R.string.precisa_add_funcionarios)
                    mEmployeeAdapter.updateFullEmployee(it)
                    progress_employee.visibility = View.GONE
                }
                else -> {
                    mEmployeeAdapter.updateFullEmployee(it)
                    mEmployeeAdapter.updateDateCurrent(date)
                    progress_employee.visibility = View.GONE
                }
            }
        })
    }

    private fun instanceCalendar(): String{
        val calendar = Calendar.getInstance().time
        val dateCalendar = SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH)
        return dateCalendar.format(calendar)
    }

    private fun removeEmployee(name: String){
        if (mViewModelEmployee.removeEmployee(name)){
            mViewModelEmployee.removePoints(name)
            showSnackBar(R.string.removido_sucesso)
            viewModel()
        }else {
            showSnackBar(R.string.nao_foi_possivel_remover)
        }
    }

    override fun clickEdit(position: Int) {
        val name = recycler_employee[position].text_nome_employee.text.toString()
        val id = businessEmployee.consultIdEmployee(name)
        val intent = Intent(this, RegisterEmployeeActivity::class.java)
        intent.putExtra(ConstantsEmployee.EMPLOYEE.COLUMNS.ID, id)
        startActivity(intent)
        finish()
    }

    override fun clickRemove(position: Int) {
        val name = recycler_employee[position].text_nome_employee.text.toString()
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Deseja remover $name?")
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Sim") { _, _ ->
            removeEmployee(name)
        }
        alertDialog.setNegativeButton("Não") { _, _ -> Snackbar.make(
            constraintLayout, getString(R.string.cancelado), Snackbar.LENGTH_LONG).show()
        }
        val dialog = alertDialog.create()
        dialog.show()
    }

    private fun showSnackBar(message: Int) {
        Snackbar.make(constraintLayout,
            message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(Color.BLACK)
            .setAction("Ok") {}
            .show()
    }
}