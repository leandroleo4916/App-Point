package com.example.app_point.business

import android.content.Context
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.entity.EmployeeEntity

class BusinessEmployee(context: Context) {

    private val mRepositoryEmployee: RepositoryEmployee = RepositoryEmployee(context)

    fun registerEmployee(employeeEntity: EmployeeEntity): String{
        return mRepositoryEmployee.conditionEmployee(employeeEntity)
    }

    fun consultEmployee(): List<String>{
        return mRepositoryEmployee.employeeList()
    }

    fun consultDataEmployee(nome: String): EmployeeEntity?{
        return mRepositoryEmployee.consultDataEmployee(nome)
    }

    fun consultIdEmployee(nome: String): Int{
        return mRepositoryEmployee.consultIdEmployee(nome)
    }

    fun consultEmployeeWithId(id: Int): EmployeeEntity?{
        return mRepositoryEmployee.consultDadosEmployeeId(id)
    }

    fun consultPhoto(nome: String): ByteArray?{
        return mRepositoryEmployee.consultPhoto(nome)
    }

    fun consultHours(nome: String): List<String>? {
        return mRepositoryEmployee.consultHours(nome)
    }
}