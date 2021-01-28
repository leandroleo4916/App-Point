package com.example.app_point.ui.dashboard

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.app_point.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.dialog_perfil.*

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val float = root.findViewById(R.id.float_bottom) as FloatingActionButton
        float.setOnClickListener { dialogEmployee() }

        return root
    }

    private fun dialogEmployee() {
        val inflater = layoutInflater
        val inflate_view = inflater.inflate(R.layout.dialog_perfil, null)

        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Cadastrar Funcionário")
        dialog.setView(inflate_view)
        dialog.setNegativeButton("Cancelar"){ dialog, which -> Toast.makeText(context,
            "Cancelado!",
            Toast.LENGTH_SHORT).show()
        }
        dialog.setPositiveButton("Salvar") { dialog, which ->
            val name = name_employee.text.toString()
            val cargo = cargo_employee.text.toString()

            saveEmployee(name, cargo)
        }
        val dialogShow = dialog.create()
        dialogShow.show()
    }

    private fun saveEmployee(name: String, cargo: String){

    }
}
