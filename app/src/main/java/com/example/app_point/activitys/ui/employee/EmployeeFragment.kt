package com.example.app_point.activitys.ui.employee

import android.app.TimePickerDialog
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.adapters.EmployeeAdapter
import com.example.app_point.constants.ConstantsUser
import com.example.app_point.databinding.ActivityToolsBinding
import com.example.app_point.databinding.FragmentEmployeeBinding
import com.example.app_point.interfaces.INotification
import com.example.app_point.interfaces.OnItemClickRecycler
import com.example.app_point.repository.RepositoryPoint
import com.example.app_point.utils.CaptureDateCurrent
import com.example.app_point.utils.ConverterPhoto
import com.example.app_point.utils.SecurityPreferences
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_employee.*
import kotlinx.android.synthetic.main.fragment_employee.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class EmployeeFragment : Fragment(), OnItemClickRecycler, INotification {

    private lateinit var employeeAdapter: EmployeeAdapter
    private val repositoryPoint: RepositoryPoint by inject()
    private val captureDateCurrent: CaptureDateCurrent by inject()
    private val viewModelEmployee by viewModel<EmployeeViewModel>()
    private val securityPreferences: SecurityPreferences by inject()

    companion object { fun newInstance() = EmployeeFragment() }

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View {
        val binding = inflater.inflate(R.layout.fragment_employee, container, false)

        recycler(binding)
        listener(binding)
        viewModel()
        observer(binding)
        touchHelper(binding)
        searchEmployeeToolbar(binding)

        return binding
    }

    private fun recycler(binding: View) {
        val recycler = binding.recycler_employee
        recycler.layoutManager = LinearLayoutManager(context)
        employeeAdapter = EmployeeAdapter(repositoryPoint, this, this, context)
        recycler.adapter = employeeAdapter
    }

    private fun listener(binding: View) {
        binding.image_back_tools.setOnClickListener { activity?.onBackPressed() }
    }

    private fun viewModel(){ viewModelEmployee.getFullEmployee() }

    private fun observer(binding: View) {
        val date = captureDateCurrent.captureDateCurrent()
        viewModelEmployee.employeeFullList.observe(viewLifecycleOwner, {
            when (it.size) {
                0 -> {
                    showSnackBar(R.string.precisa_add_funcionarios)
                    employeeAdapter.updateFullEmployee(it, date)
                    binding.progress_employee.visibility = View.GONE
                }
                else -> {
                    employeeAdapter.updateFullEmployee(it, date)
                    binding.progress_employee.visibility = View.GONE
                }
            }
        })
    }

    private fun searchEmployeeToolbar(binding: View) {

        binding.toolbar_ferramentas.inflateMenu(R.menu.menu_search)
        val searchItem = binding.toolbar_ferramentas.menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint.run {
            Html.fromHtml("<font color = #e6e1e4>" + resources.getString(R.string.pesquisar),
                resources.getColor(R.color.letras))
        }

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.toolbar_ferramentas.menu.findItem(R.id.action_search).collapseActionView()
                employeeAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                employeeAdapter.filter.filter(newText)
                return true
            }
        })
    }

    override fun clickEdit(id: Int) {
        //val intent = Intent(this, RegisterEmployeeActivity::class.java)
        //intent.putExtra(ConstantsEmployee.EMPLOYEE.COLUMNS.ID, id)
        //startActivity(intent)
    }

    private fun createDialog(message: String): AlertDialog.Builder? {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle(message)
        builder?.setCancelable(false)
        return builder
    }

    override fun clickRemove(id: Int, name: String, position: Int){

        val inflate = layoutInflater.inflate(R.layout.dialog_confim_password, null)
        val textPassword = inflate.findViewById(R.id.text_password) as TextInputEditText

        val dialog = createDialog("Deseja remover $name? Confirme a senha!")
        dialog?.setView(inflate)
        dialog?.setPositiveButton("Confirmar") { _, _ ->
            val password = securityPreferences.getStoredString(ConstantsUser.USER.COLUNAS.PASSWORD)
            if (password == textPassword.text.toString()){
                removeEmployee(id, name)
                employeeAdapter.notifyItemRemoved(position)
            }
            else{
                showSnackBar(R.string.erro_senha)
                employeeAdapter.notifyDataSetChanged()
            }
        }
        dialog?.setNegativeButton("Cancelar") { _, _ ->
            showSnackBar(R.string.cancelado)
            employeeAdapter.notifyDataSetChanged()
        }
        dialog?.create()?.show()
    }

    override fun clickNext(name: String, date: String, position: Int) {
        reactBackAndNext(name, date, position, 1)
    }

    override fun clickBack(name: String, date: String, position: Int) {
        reactBackAndNext(name, date, position, -1)
    }

    override fun clickHour(
        name: String,
        date: String,
        positionHour: Int,
        hour: String,
        position: Int
    ) {

        val inflate = layoutInflater.inflate(R.layout.dialog_add_hour_manual, null)
        val clock = inflate.findViewById<TextView>(R.id.textView_hour)

        if (hour == "--:--"){ clock.text = captureDateCurrent.captureHoraCurrent() }
        else { clock.text = hour }

        val dialog = createDialog("Clique na hora para editar!")
        dialog?.setView(inflate)
        dialog?.setPositiveButton("Editar") { _, _ ->

            if (date == "Hoje") {
                val dateToday = captureDateCurrent.captureDateCurrent()
                editPoint(name, dateToday, positionHour, clock.text.toString(), position)
            }
            else{ editPoint(name, date, positionHour, clock.text.toString(), position) }
        }
        dialog?.setNegativeButton("Cancelar") { _, _ ->
            showSnackBar(R.string.cancelado)
        }
        dialog?.create()?.show()

        clock.setOnClickListener {
            val cal = java.util.Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(java.util.Calendar.HOUR_OF_DAY, hour)
                cal.set(java.util.Calendar.MINUTE, minute)

                clock.text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(cal.time)
            }
            TimePickerDialog(context, timeSetListener, cal.get(java.util.Calendar.HOUR_OF_DAY),
                cal.get(java.util.Calendar.MINUTE), true).show()
        }
    }

    override fun clickImage(image: ByteArray, name: String) {
        val inflate = layoutInflater.inflate(R.layout.view_image_employee, null)
        val photo = inflate.findViewById<ImageView>(R.id.image_click)
        val photoConverter = ConverterPhoto().converterToBitmap(image)
        photo.setImageBitmap(photoConverter)
        val dialog = createDialog(name)
        dialog?.setView(inflate)
        dialog?.setPositiveButton(R.string.sair){ _, _ -> }
        dialog?.create()?.show()
    }

    private fun editPoint(name:String, date:String, positionHour:Int, hour:String, position:Int){

        when (viewModelEmployee.editPoint(name, date, positionHour, hour)){
            true -> {
                showSnackBar(R.string.pontos_editado)
                employeeAdapter.updateHour(positionHour, hour, position)
            }
            else -> showSnackBar(R.string.nao_foi_possivel_editar)
        }
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

        employeeAdapter.updateDate(point, dateCurrent, dateFinal, position)
    }

    private fun addOrRemoveDate(date: String, pos: Int): String{
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val divDate = date.split("/")

        val cal = Calendar.getInstance()
        cal.set(divDate[2].toInt(), divDate[1].toInt() - 1, divDate[0].toInt())
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

    private fun touchHelper(binding: View) {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.START or ItemTouchHelper.END) {

            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                val source = viewHolder.bindingAdapterPosition
                val destination = target.bindingAdapterPosition
                employeeAdapter.swapPosition(source, destination)
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                employeeAdapter.removeEmployee(position)
            }
        }

        val recycler = binding.recycler_employee
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycler)
    }

    override fun notification() { showSnackBar(R.string.nenhum_funcionario_encontrado) }

    private fun showSnackBar(message: Int) {
        Snackbar.make(container_employee,
            message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(Color.BLACK)
            .setAction("Ok") {}
            .show()
    }
}