package com.example.app_point.fragments.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.model.PointsAdapter
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.repository.RepositoryPoint
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mRepositoryEmployee: RepositoryEmployee
    private lateinit var mRepositoryPoint: RepositoryPoint
    private var mPoints: PointsAdapter = PointsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val float = root.findViewById(R.id.float_button_point) as FloatingActionButton
        float.setOnClickListener { loadDialogPoint() }

        val recycler = root.findViewById<RecyclerView>(R.id.recycler_points)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = mPoints

        mRepositoryEmployee = RepositoryEmployee(context)
        mRepositoryPoint = RepositoryPoint(context)

        homeViewModel.getEmployeeList()
        homeViewModel.getHourList()
        homeViewModel.getDateList()

        observe()

        return root
    }
    private fun observe(){
        homeViewModel.listEmployee.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            mPoints.updateEmployee(it)
        })
        homeViewModel.listHours.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            mPoints.updateHours(it)
        })
        homeViewModel.listDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            mPoints.updateDate(it)
        })
    }

    private fun loadDialogPoint() {
        val date = Calendar.getInstance().time
        val dateTime = SimpleDateFormat("d/MM/YYYY", Locale.ENGLISH)
        // Current hour capture
        val hora = SimpleDateFormat("HH:mm")
        val hourAtual: String = hora.format(date)

        // Current date capture
        val dateAtual = dateTime.format(date)

        val inflater = layoutInflater
        val inflateView = inflater.inflate(R.layout.dialog_point, null)
        val textData = inflateView.findViewById(R.id.datePoint) as TextView
        textData.text = dateTime.format(date)

        // Captures list of employees and adds to the spinner
        val list = mRepositoryEmployee.getEmployeeList()
        val listSpinner= inflateView.findViewById(R.id.spinnerGetEmployee) as Spinner
        val adapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, list) }
        listSpinner.adapter = adapter
        listSpinner.onItemSelectedListener = this

        // Create dialog
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Bater Ponto")
        alertDialog.setView(inflateView)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Registrar") {
                dialog, which ->

            //Capture Spinner item
            val itemSpinner = listSpinner.selectedItem.toString()
            registerPoints(hourAtual, dateAtual, itemSpinner)

        }
        alertDialog.setNegativeButton(getString(R.string.cancelar)) {
                dialog, which -> Toast.makeText(context, getString(R.string.cancelado), Toast.LENGTH_SHORT).show()
        }
        val dialog = alertDialog.create()
        dialog.show()
    }
    private fun registerPoints(hour: String, date: String, employee: String) {
        when {
            mRepositoryPoint.getPoint(hour, date, employee) -> {
                Toast.makeText(context, R.string.cadastro_feito, Toast.LENGTH_SHORT).show()
                homeViewModel.getEmployeeList()
                homeViewModel.getHourList()
                homeViewModel.getDateList()
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent?.id) {
            R.id.spinnerGetEmployee -> {
                parent.getItemAtPosition(position).toString()
            }
        }
    }
    override fun onNothingSelected(parent: AdapterView<*>?){}
}