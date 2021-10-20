package com.example.app_point.activitys

import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_point.R
import com.example.app_point.adapters.EmployeeAdapter
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.databinding.ActivityToolsBinding
import com.example.app_point.interfaces.OnItemClickRecycler
import com.example.app_point.model.ViewModelEmployee
import com.example.app_point.repository.RepositoryPoint
import com.example.app_point.utils.CaptureDateCurrent
import com.example.app_point.utils.createDialog
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class ToolsActivity : AppCompatActivity(), OnItemClickRecycler {

    private lateinit var mEmployeeAdapter: EmployeeAdapter
    private val repositoryPoint: RepositoryPoint by inject()
    private val captureDateCurrent: CaptureDateCurrent by inject()
    private val viewModelEmployee by viewModel<ViewModelEmployee>()
    private val binding by lazy { ActivityToolsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recycler()
        listener()
        viewModel()
        observer()
    }

    private fun recycler(){
        val recycler = binding.recyclerEmployee
        recycler.layoutManager = LinearLayoutManager(this)
        mEmployeeAdapter = EmployeeAdapter(repositoryPoint, this)
        recycler.adapter = mEmployeeAdapter
    }

    private fun listener(){ binding.imageBackTools.setOnClickListener { finish() } }

    private fun viewModel(){ viewModelEmployee.getFullEmployee() }

    private fun observer(){
        val date = captureDateCurrent.captureDateCurrent()
        viewModelEmployee.employeeFullList.observe(this, {
            when (it.size) {
                0 -> {
                    showSnackBar(R.string.precisa_add_funcionarios)
                    mEmployeeAdapter.updateFullEmployee(it, date)
                    binding.progressEmployee.visibility = View.GONE
                }
                else -> {
                    mEmployeeAdapter.updateFullEmployee(it, date)
                    binding.progressEmployee.visibility = View.GONE
                }
            }
        })
    }

    override fun clickEdit(id: Int) {
        val intent = Intent(this, RegisterEmployeeActivity::class.java)
        intent.putExtra(ConstantsEmployee.EMPLOYEE.COLUMNS.ID, id)
        startActivity(intent)
        finish()
    }

    override fun clickRemove(id: Int, name: String) {
        val alertDialog = createDialog("Deseja remover $name?")
        alertDialog.setPositiveButton("Sim") { _, _ -> removeEmployee(id, name) }
        alertDialog.setNegativeButton("Não") { _, _ -> showSnackBar(R.string.cancelado) }
        alertDialog.create().show()
    }

    override fun clickNext(name: String, date: String, position: Int) {
        reactBackAndNext(name, date, position, 1)
    }

    override fun clickBack(name: String, date: String, position: Int) {
        reactBackAndNext(name, date, position, -1)
    }

    private fun reactBackAndNext(name: String, dateCaptured: String, position: Int, pos: Int){

        val date =
            if (dateCaptured == "Hoje") {
                val dateToday = captureDateCurrent.captureDateCurrent()
                addOrRemoveDate(dateToday, pos)
            } else{ addOrRemoveDate(dateCaptured, pos) }

        addItemToView(name, date, position)
    }

    private fun addItemToView(name: String, dateFinal: String, position: Int){
        val point = viewModelEmployee.consultPoint(name, dateFinal)
        val dateCurrent = captureDateCurrent.captureDateCurrent()

        mEmployeeAdapter.updateDate(point, dateCurrent, dateFinal, position)
    }

    private fun addOrRemoveDate(date: String, pos: Int): String{
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val divDate = date.split("/")

        val cal = Calendar.getInstance()
        cal.set(divDate[2].toInt(), divDate[1].toInt()-1, divDate[0].toInt())
        cal.add(Calendar.DAY_OF_MONTH, pos)

        return dateFormat.format(cal.time)
    }

    private fun removeEmployee(id: Int, name: String){
        if (viewModelEmployee.removeEmployee(id)){
            viewModelEmployee.removePoints(name)
            showSnackBar(R.string.removido_sucesso)
            viewModel()
        }else { showSnackBar(R.string.nao_foi_possivel_remover) }
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