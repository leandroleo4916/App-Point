package com.example.app_point.activitys

import android.content.Intent
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
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.interfaces.OnItemClickRecycler
import com.example.app_point.model.*
import com.example.app_point.utils.ConverterPhoto
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_tools.*
import kotlinx.android.synthetic.main.recycler_employee.*
import kotlinx.android.synthetic.main.recycler_employee.view.*
import java.text.SimpleDateFormat
import java.util.*

class ToolsActivity : AppCompatActivity(), View.OnClickListener, OnItemClickRecycler {

    private lateinit var mEmployeeAdapter: EmployeeAdapter
    private lateinit var mViewModelEmployee: ViewModelEmployee
    private lateinit var constraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tools)

        mViewModelEmployee = ViewModelProvider(this).get(ViewModelEmployee::class.java)
        constraintLayout = findViewById(R.id.container_employee)

        val recycler = findViewById<RecyclerView>(R.id.recycler_employee)
        recycler.layoutManager = LinearLayoutManager(this)
        mEmployeeAdapter = EmployeeAdapter(application, this)
        recycler.adapter = mEmployeeAdapter

        listener()
        viewModel()
        observer()
    }

    private fun listener(){
        image_back_tools.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            image_back_tools -> finish()
        }
    }

    private fun viewModel(){
        mViewModelEmployee.getFullEmployee()
    }

    private fun observer(){
        val date = instanceCalendar()
        mViewModelEmployee.employeeFullList.observe(this, {
            when (it.size) {
                0 -> { Snackbar.make(constraintLayout, getString(R.string.precisa_add_funcionarios),
                    Snackbar.LENGTH_LONG).show() }
                else -> {
                    mEmployeeAdapter.updateFullEmployee(it)
                    mEmployeeAdapter.updateDateCurrent(date)
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
            Snackbar.make(constraintLayout, getString(R.string.removido_sucesso),
                Snackbar.LENGTH_LONG).show()
            viewModel()
        }else {
            Snackbar.make(constraintLayout, getString(R.string.nao_foi_possivel_remover),
                Snackbar.LENGTH_LONG).show()
        }
    }

    override fun clickEdit(position: Int) {
        val name = recycler_employee[position].text_nome_employee.text.toString()
        val intent = Intent(this, RegisterEmployeeActivity::class.java)
        intent.putExtra(ConstantsEmployee.EMPLOYEE.COLUMNS.NAME, name)
        startActivity(intent)
        finish()
    }

    override fun clickRemove(position: Int) {
        val name = recycler_employee[position].text_nome_employee.text.toString()
        removeEmployee(name)
    }
}