package com.example.app_point.activitys.ui.points

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_point.R
import com.example.app_point.adapters.PointsAdapter
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.interfaces.IHideNavView
import com.example.app_point.interfaces.ItemEmployee
import com.example.app_point.repository.RepositoryPoint
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_points.view.*
import org.koin.android.ext.android.inject

class PointsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var hideNav: IHideNavView
    private val mPointsAdapter: PointsAdapter by inject()
    private val mListEmployee: BusinessEmployee by inject()
    private val repository: RepositoryPoint by inject()
    private lateinit var pointsViewModel: PointsViewModel
    private lateinit var binding: View

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View {
        binding = inflater.inflate(R.layout.fragment_points, container, false)
        pointsViewModel = PointsViewModel(repository)

        recycler()
        searchPoints()
        listener()
        observe()
        showNavView(binding)

        return binding
    }

    private fun showNavView(binding: View) {

        var isShow = true
        var scrollRange = -1
        val appBar = binding.appbar
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1){
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0){
                if (context is ItemEmployee) { hideNav = context as IHideNavView }
                hideNav.hideNavView(false)
                isShow = true
            } else if (isShow){
                if (context is ItemEmployee) { hideNav = context as IHideNavView }
                hideNav.hideNavView(true)
                isShow = false
            }
        })
    }

    private fun recycler() {
        val recycler = binding.recycler_activity_pontos
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = mPointsAdapter
    }

    private fun searchPoints() {
        pointsViewModel.getFullEmployee("")
    }

    private fun observe() {
        pointsViewModel.employeeFullList.observe (viewLifecycleOwner, {
            when (it.size) {
                0 -> {
                    mPointsAdapter.updateFullPoints(it)
                    binding.progress_ponto.visibility = View.GONE
                }
                else -> {
                    mPointsAdapter.updateFullPoints(it)
                    binding.progress_ponto.visibility = View.GONE
                    binding.image_need_add_point.visibility = View.INVISIBLE
                    binding.text_need_add_point.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun listener() {
        binding.image_filter.setOnClickListener { dialogPoint() }
    }

    private fun dialogPoint() {
        val inflateView = layoutInflater.inflate(R.layout.dialog_list_employee, null)
        val list = mListEmployee.consultEmployee()
        val listSpinner = inflateView.findViewById(R.id.spinner_employee) as Spinner
        val adapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, list) }
        listSpinner.adapter = adapter
        listSpinner.onItemSelectedListener = this

        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle(getString(R.string.filtrar_funcionario))
        alertDialog.setView(inflateView)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton(getString(R.string.filtrar)) { _, _ ->

            when (val itemSpinner = listSpinner.selectedItem) {
                null -> showSnackBar(R.string.precisa_add_funcionarios)
                else -> {
                    pointsViewModel.getFullEmployee(itemSpinner.toString())
                    binding.textView_toolbar.text = itemSpinner.toString()
                }
            }
        }
        alertDialog.setNegativeButton(getString(R.string.todos)) { _, _ ->
            when (listSpinner.selectedItem) {
                null -> showSnackBar(R.string.precisa_add_funcionarios)
                else -> {
                    pointsViewModel.getFullEmployee("")
                    binding.textView_toolbar.setText(R.string.pontos_registrados)
                }
            }
        }
        alertDialog.create().show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent?.id) {
            R.id.spinner_employee -> parent.getItemAtPosition(position).toString()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun showSnackBar(message: Int) {
        Snackbar.make(binding.container_points,
            message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(Color.BLACK)
            .setAction("Ok") {}
            .show()
    }
}