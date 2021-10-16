package com.example.app_point.activitys

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_point.R
import com.example.app_point.adapters.EmployeeAdapter
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.databinding.ActivityToolsBinding
import com.example.app_point.interfaces.OnItemClickRecycler
import com.example.app_point.model.ViewModelEmployee
import com.example.app_point.utils.CaptureDateCurrent
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.recycler_employee.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class ToolsActivity : AppCompatActivity(), OnItemClickRecycler {

    private lateinit var mEmployeeAdapter: EmployeeAdapter
    private val captureDateCurrent: CaptureDateCurrent by inject()
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
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Deseja remover $name?")
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Sim") { _, _ -> removeEmployee(id, name) }
        alertDialog.setNegativeButton("Não") { _, _ -> showSnackBar(R.string.cancelado) }
        val dialog = alertDialog.create()
        dialog.show()
    }

    override fun clickNext(name: String, position: Int) { reactBackAndNext(name, position, 1) }

    override fun clickBack(name: String, position: Int) { reactBackAndNext(name, position, -1) }

    private fun reactBackAndNext(name: String, position: Int, pos: Int){

        val dateCaptured = binding.recyclerEmployee[position].text_data_ponto.text.toString()
        val dateFinal =
            if (dateCaptured == "Hoje") {
                val dateToday = captureDateCurrent.captureDateCurrent()
                addOrRemoveDate(dateToday, pos)
            } else{ addOrRemoveDate(dateCaptured, pos) }

        addItemToView(name, dateFinal, position)
    }

    private fun addItemToView(name: String, dateFinal: String, position: Int){
        val point = viewModelEmployee.consultPoints(name, dateFinal)
        val date = captureDateCurrent.captureDateCurrent()

        when {
            dateFinal != date -> { binding.recyclerEmployee[position].text_data_ponto.text = dateFinal }
            else -> { binding.recyclerEmployee[position].text_data_ponto.text = getString(R.string.hoje) }
        }

        binding.run {
            recyclerEmployee[position].text_hora1.text = point?.hora1 ?: "--:--"
            recyclerEmployee[position].text_hora2.text = point?.hora2 ?: "--:--"
            recyclerEmployee[position].text_hora3.text = point?.hora3 ?: "--:--"
            recyclerEmployee[position].text_hora4.text = point?.hora4 ?: "--:--"
        }
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