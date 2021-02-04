package com.example.app_point.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.model.ViewModel
import kotlinx.android.synthetic.main.activity_pontos.*
import kotlinx.android.synthetic.main.activity_register_employee.*
import kotlinx.android.synthetic.main.activity_tools.*

class PontosActivity : AppCompatActivity(), View.OnClickListener {

    private val mPontosAdapter: PontosAdapter = PontosAdapter()
    private val mViewModel: ViewModel = ViewModel(application)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pontos)

        val recycler = findViewById<RecyclerView>(R.id.recycler_activity_pontos)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = mPontosAdapter

        mViewModel.getEmployee()
        mViewModel.getData()
        mViewModel.getHora()

        listener()
        observe()
    }

    private fun observe(){
        mViewModel.employeeList.observe(this, androidx.lifecycle.Observer {
            mPontosAdapter.updateFuncionario(it)
        })
        mViewModel.dataList.observe(this, androidx.lifecycle.Observer {
            mPontosAdapter.updateData(it)
        })
        mViewModel.horaList.observe(this, androidx.lifecycle.Observer {
            mPontosAdapter.updateHora(it)
        })
    }

    private fun listener(){
        image_back_pontos.setOnClickListener(this)
        recycler_activity_pontos.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            image_back_pontos -> finish()
            recycler_activity_pontos -> Toast.makeText(this,
                "Falta implementar esta chamada!", Toast.LENGTH_SHORT).show()
        }
    }

}