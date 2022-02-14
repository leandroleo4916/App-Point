package com.example.app_point.activitys.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_point.utils.CalculateHours
import com.example.app_point.entity.*
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.repository.RepositoryPoint
import com.example.app_point.utils.CaptureDateCurrent

class DashboardViewModel (private var points: RepositoryPoint,
                          private var employee: RepositoryEmployee,
                          private val captureDateCurrent: CaptureDateCurrent,
                          private val calculateHours: CalculateHours): ViewModel() {

    private val totalEmployeeList = MutableLiveData<Int>()
    val employeeTotal: LiveData<Int> = totalEmployeeList

    private val totalEmployeeVacation = MutableLiveData<Int>()
    val employeeVacation: LiveData<Int> = totalEmployeeVacation

    private val totalEmployeeDisabled = MutableLiveData<Int>()
    val employeeDisabled: LiveData<Int> = totalEmployeeDisabled

    private val totalEmployeeWorking = MutableLiveData<Int>()
    val employeeWorking: LiveData<Int> = totalEmployeeWorking

    private val totalDetailEmployee = MutableLiveData<ArrayList<EntityDashboard>>()
    val employeeDetail: LiveData<ArrayList<EntityDashboard>> = totalDetailEmployee

    private val totalBestEmployee = MutableLiveData<ArrayList<EntityBestEmployee>>()
    val employeeBest: LiveData<ArrayList<EntityBestEmployee>> = totalBestEmployee

    fun consultEmployeeAndPoints(){

        val employee = employee.consultFullEmployee()
        val listEmployeeHoursExtraAndDone: ArrayList<EntityDashboard> = arrayListOf()
        val bestEmployee: ArrayList<EntityBestEmployee> = arrayListOf()
        var vacation = 0
        var disabled = 0
        var working = 0

        for (i in employee){

            val totalHour = points.consultTotalExtraByIdEmployee(i.id)
            val extra = totalHour?.get(0) ?: 0
            val feita = totalHour?.get(0) ?: 0

            var dateFirst = captureDateCurrent.captureFirstDayOfMonth()
            val dateCurrent = captureDateCurrent.captureDateCurrent()
            val dateTomorrow = captureDateCurrent.captureNextDate(dateCurrent)
            var punctuation = 0
            val cadastro: Float = consultNullOrNotNull(i)

            while (dateFirst != dateTomorrow){

                val point = consultPoint(i.id, dateFirst)
                punctuation += when {
                    point == null -> { 0 }
                    point.punctuation == null -> { 0 }
                    else -> { point.punctuation }
                }

                dateFirst = captureDateCurrent.captureNextDate(dateFirst)
            }

            when (i.status) {
                "Trabalhando" -> { working += 1 }
                "De férias" -> { vacation += 1 }
                else -> { disabled += 1 }
            }

            listEmployeeHoursExtraAndDone.add(EntityDashboard(i.photo, extra, feita, cadastro))
            bestEmployee.add(EntityBestEmployee(i.photo, punctuation))

        }

        totalEmployeeList.value = employee.size
        totalEmployeeVacation.value = vacation
        totalEmployeeDisabled.value = disabled
        totalEmployeeWorking.value = working
        totalDetailEmployee.value = listEmployeeHoursExtraAndDone
        totalBestEmployee.value = bestEmployee

    }

    private fun consultNullOrNotNull(i: EmployeeEntityFull): Float{

        var value = 0F

        if (i.rg == 0) value += 1
        if (i.cpf == 0) value += 1
        if (i.ctps == 0) value += 1
        if (i.salario == 0) value += 1
        if (i.estadocivil == null) value += 1

        return (((19.00 - value) / 19.00) * 100.00).toFloat()
    }

    private fun consultPoint(id: Int, date: String): HourEntityInt? {
        return points.selectPointInt(id, date)
    }
}