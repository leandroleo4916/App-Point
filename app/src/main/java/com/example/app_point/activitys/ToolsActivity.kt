package com.example.app_point.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.model.*
import com.example.app_point.utils.ConverterPhoto
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_tools.*

class ToolsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mEmployeeAdapter: EmployeeAdapter
    private lateinit var mViewModelEmployee: ViewModelEmployee
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storag: FirebaseStorage
    //val mConverter: ConverterPhoto = ConverterPhoto()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tools)

        mViewModelEmployee = ViewModelProvider(this).get(ViewModelEmployee::class.java)
        constraintLayout = findViewById(R.id.container_employee)


        val recycler = findViewById<RecyclerView>(R.id.recycler_employee)
        recycler.layoutManager = LinearLayoutManager(this)
        mEmployeeAdapter = EmployeeAdapter(application)
        recycler.adapter = mEmployeeAdapter

        firestore = Firebase.firestore
        storag = Firebase.storage
        listener()
        viewModel()
        observer()
        //buscarListEmployee()
    }
    /*
    fun buscarListEmployee(){
        val list = firestore.collection("funcionarios")

        list.get()
            .addOnSuccessListener { result ->
                for (element in result){
                    progress_employee.visibility = View.GONE
                    val photo = element["photo"]
                    val convert = photo?.let { mPhoto -> mConverter.converterStringToBitmap(mPhoto) }
                }
            }
            .addOnFailureListener {
                Snackbar.make(constraintLayout, getString(R.string.precisa_add_funcionarios), Snackbar.LENGTH_LONG).show()
            }
    }
    */
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
            Thread.sleep(1000)

            runOnUiThread {
                val list = firestore.collection("funcionários")
                list.get()
                    .addOnSuccessListener { result ->
                        val res = result.documents
                        mViewModelEmployee.getFullEmployee(result)
                        progress_employee.visibility = View.GONE

                    }
                    .addOnFailureListener {
                        Snackbar.make(constraintLayout, getString(R.string.precisa_add_funcionarios),
                            Snackbar.LENGTH_LONG).show()
                        progress_employee.visibility = View.GONE
                    }

            }
        }.start()
    }

    private fun observer(){
        mViewModelEmployee.employeeFullList.observe(this, {
            when (it) {
                null -> { Snackbar.make(constraintLayout, getString(R.string.precisa_add_funcionarios),
                    Snackbar.LENGTH_LONG).show() }
                else -> { mEmployeeAdapter.updateFullEmployee(it) }
            }
        })
    }
}