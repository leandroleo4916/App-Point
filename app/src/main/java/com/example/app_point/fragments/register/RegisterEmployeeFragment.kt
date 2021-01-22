package com.example.app_point.fragments.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.app_point.R
import com.example.app_point.model.EmployeeAdapter
import com.example.app_point.repository.RepositoryEmployee
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RegisterEmployeeFragment : Fragment() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var mReposistoryEmployee: RepositoryEmployee
    private val mAdapterEmployee: EmployeeAdapter = EmployeeAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        //val float = root.findViewById(R.id.float_button_employee) as FloatingActionButton
        //float.setOnClickListener { showDialog() }

        // Cria a recycler
        //val recycler = root.findViewById<RecyclerView>(R.id.recyclerEmployee)
        //recycler.layoutManager = LinearLayoutManager(context)
        //recycler.adapter = mAdapterEmployee

        mReposistoryEmployee = RepositoryEmployee(context)
        registerViewModel.getEmployeeList()
        registerViewModel.getOfficeList()

        observer()
        return root
    }

    private fun observer() {
        registerViewModel.textEmployee.observe(viewLifecycleOwner, Observer {
            mAdapterEmployee.updateEmployee(it)
        })
        registerViewModel.textOffice.observe(viewLifecycleOwner, Observer {
            mAdapterEmployee.updateOffice(it)
        })
    }

}