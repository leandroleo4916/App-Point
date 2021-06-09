package com.example.app_point.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.model.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_tools.*

class ToolsActivity : AppCompatActivity(), View.OnClickListener {

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
        mEmployeeAdapter = EmployeeAdapter(application)
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
        Thread{
            // Block Thread
            Thread.sleep(1000)
            runOnUiThread {
                mViewModelEmployee.getFullEmployee()
                //mViewModelEmployee.getFullPoints(name, "")
                //text_date_selected.text = getString(R.string.todos)
                progress_employee.visibility = View.GONE
            }
        }.start()
    }

    private fun observer(){
        mViewModelEmployee.employeeFullList.observe(this, {
            when (it.size) {
                0 -> { Snackbar.make(constraintLayout, getString(R.string.precisa_add_funcionarios),
                    Snackbar.LENGTH_LONG).show() }
                else -> { mEmployeeAdapter.updateFullEmployee(it) }
            }
        })
    }
}